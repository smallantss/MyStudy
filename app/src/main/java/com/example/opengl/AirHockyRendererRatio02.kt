package com.example.opengl

import android.content.Context
import android.opengl.*
import android.opengl.GLES20.*
import android.opengl.Matrix.orthoM
import com.example.mystudy.R
import com.example.mystudy.ui.loge
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * 添加颜色
 */
class AirHockyRendererRatio02(val context: Context) : GLSurfaceView.Renderer {

    //长方形改为四个三角形，6个点(两个重合) 直线 两个点 一个点 前面两个代表坐标，后面三个代表颜色
    private val tableVerticesTriangles = floatArrayOf(
            0f, 0f, 1f, 1f, 1f,
            -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
            0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
            0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
            -0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
            -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,

            -0.5f, 0f, 1f, 0f, 0f,
            0.5f, 0f, 0f, 1f, 0f,

            0f, -0.4f, 0f, 0f, 1f,
            0f, 0.4f, 1f, 0f, 0f
    )

    //提供给native使用，避免java数据被回收
    //分配一块本地内存，这块内存不会被GC管理，需要知道大小
    private val vertexData: FloatBuffer = ByteBuffer
            .allocateDirect(tableVerticesTriangles.size * BYTES_PER_FLOAT)
            //按照本地字节序组织内容
            .order(ByteOrder.nativeOrder())
            //转为FloatBuffer避免直接操作字节
            .asFloatBuffer()

    init {
        //把数据从Davlik内存复制到本地内存，进程结束时内存会被释放掉
        vertexData.put(tableVerticesTriangles)
    }

    companion object {
        //每个float占4个字节大小
        const val BYTES_PER_FLOAT = 4
    }

    private val U_MATRIX = "u_Matrix"
    private val projectionMatrix = FloatArray(16)
    private var uMatrixPosition: Int = 0

    private val A_POSITION = "a_Position"
    private var aPositionLocation: Int = 0

    //获取a_Color在OpenGL位置中的变量
    private val A_COLOR = "a_Color"
    private var aColorLocation: Int = 0
    private val COLOR_COMPONENT_COUNT = 3
    private val POSITION_COMPONENT_COUNT = 2

    //表示从一个顶点到下一个顶点跳过的颜色数据
    private val STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        glClearColor(0.0f, 0f, 0f, 0f)
        //读取着色器资源
        val vertexShaderSource = readTextFileFromResource(context, R.raw.simple_vertex_shader_ratio)
        val fragmentShaderSource = readTextFileFromResource(context, R.raw.simple_fragment_shader_ratio)
        //创建顶点着色器和片段着色器
        val vertexShader = compileVertexShader(vertexShaderSource)
        val fragmentShader = compileFragmentShader(fragmentShaderSource)
        //链接顶点着色器和片段着色器到Program上
        val program = linkProgram(vertexShader, fragmentShader)
        //验证Program
        validateProgram(program)
        //使用Program
        glUseProgram(program)
        //获取属性的位置
        aColorLocation = glGetAttribLocation(program, A_COLOR)
        //获取属性的位置
        aPositionLocation = glGetAttribLocation(program, A_POSITION)
        uMatrixPosition = glGetUniformLocation(program, U_MATRIX)
        //移动到开始位置，找到position
        vertexData.position(0)
        //在缓冲区中找到a_Position对应的数据 属性位置 一个顶点关联多少个分量(两个，x和y) 数据类型 整型时才有效 多个属性才有效 去哪里读
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, vertexData)
        glEnableVertexAttribArray(aPositionLocation)

        //移动到颜色位置，找到颜色
        vertexData.position(POSITION_COMPONENT_COUNT)
        glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, vertexData)
        //去哪里找数据
        glEnableVertexAttribArray(aColorLocation)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height)
        //创建正交投影矩阵
        if (width > height) {
            val ratio = width / height.toFloat()
            orthoM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, -1f, 1f)
        } else {
            val ratio = height / width.toFloat()
            orthoM(projectionMatrix, 0, -1f, 1f, -ratio, ratio, -1f, 1f)
        }
    }

    override fun onDrawFrame(gl: GL10?) {
        glClear(GL_COLOR_BUFFER_BIT)
        glUniformMatrix4fv(uMatrixPosition, 1, false, projectionMatrix, 0)
        //绘制三角形
        //更新u_Color，因为是vec4,因此要四个值，指定颜色
//        glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f)
        //绘制什么形状 从顶点数组的开头开始读顶点 读取6个顶点
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6)

        //绘制直线
        ////更新u_Color，因为是vec4,因此要四个值，指定颜色
//        glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f)
        //绘制直线 从6开始读 读取2个
        glDrawArrays(GL_LINES, 6, 2)

        //绘制两个点
//        glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f)
        glDrawArrays(GL_POINTS, 8, 1)

//        glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f)
        glDrawArrays(GL_POINTS, 9, 1)

    }

}