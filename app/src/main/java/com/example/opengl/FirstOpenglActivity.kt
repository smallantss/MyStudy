package com.example.opengl

import android.app.ActivityManager
import android.content.Context
import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.example.mystudy.R
import com.example.mystudy.ui.loge
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL
import javax.microedition.khronos.opengles.GL10
import kotlin.properties.Delegates

class FirstOpenglActivity : AppCompatActivity() {

    private lateinit var glSurfaceView: GLSurfaceView
    private var rendererSet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_first_opengl)
        glSurfaceView = GLSurfaceView(this)

        val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val supportsEs2 = am.deviceConfigurationInfo.reqGlEsVersion >= 0x20000
        if (supportsEs2) {
            glSurfaceView.setEGLContextClientVersion(2)
            glSurfaceView.setRenderer(AirHockyRenderer(this))
            rendererSet = true
        } else {
            Toast.makeText(this, "device not support OpenGL ES 2.0", Toast.LENGTH_SHORT).show()
        }
        setContentView(glSurfaceView)
    }

    override fun onPause() {
        super.onPause()
        if (rendererSet) {
            glSurfaceView.onPause()
        }
    }

    override fun onResume() {
        super.onResume()
        if (rendererSet) {
            glSurfaceView.onResume()
        }
    }
}

//GLSurfaceView在一个单独的线程中渲染，因此主线程的实例通过queueEvent传给后台渲染线程，渲染线程通过runOnUiThread回到主线程
class FirstOpenGLRenderer : GLSurfaceView.Renderer {
    //可能被GLSurfaceView调用多次，在Surface创建和其他Activity切换回来调用
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        loge("onSurfaceCreated")
        //清空屏幕，参数为RGBA
        glClearColor(1.0f, 1.0f, 0.0f, 0.0f)
    }

    //Surface创建以后，每次Surface尺寸变化，均会被GLSurfaceView调用，如横竖屏切换
    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        loge("onSurfaceChanged:w $width,h $height")
        //设置视图尺寸
        glViewport(0, 0, width, height)
    }

    //绘制一帧时，会被GLSurfaceView调用。一定要绘制内容，即使是情空屏幕，如果什么都没画，会闪烁
    override fun onDrawFrame(gl: GL10?) {
        loge("onDrawFrame")
        //擦除屏幕颜色，使用glClearColor填充屏幕
//        glClear(GL_COLOR_BUFFER_BIT)
    }

}

/**
 * 曲棍球游戏
 * OpenGL只能绘制 点、直线、三角形
 * 定义三角形的时候以逆时针顺序排列顶点，这样称为卷曲顺序，可以优化性能。
 * 卷曲顺序可以指出一个三角形属于任何给定物体的前面或者后面。OpenGL可以忽略那些无法被看到的后面的三角形。
 */
class AirHockyRenderer(val context: Context) : GLSurfaceView.Renderer {

    //桌子长方形的四个点坐标
    private val tableVertices = floatArrayOf(0f, 0f, 0f, 14f, 9f, 14f, 9f, 0f)

    //变为两个三角形坐标
    private val tableVerticesTriangles = floatArrayOf(
            -0.5f, -0.5f,
            0.5f, 0.5f,
            -0.5f, 0.5f,
            -0.5f, -0.5f,
            0.5f, -0.5f,
            0.5f, 0.5f,

            -0.5f, 0f,
            0.5f, 0f,

            0f, -0.25f,
            0f, 0.25f,

            0f,0f
    )

    //中间分割线,及两边两个点
    private val line = floatArrayOf(0f, 7f, 9f, 7f, 4.5f, 2f, 4.5f, 12f)

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

    //保持u_Color在OpenGL位置中的变量
    private val U_COLOR = "u_Color"
    private var uColorLocation: Int = 0

    private val A_POSITION = "a_Position"
    private var aPositionLocation: Int = 0

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        glClearColor(0.0f, 0f, 0f, 0f)
        //读取着色器资源
        val vertexShaderSource = readTextFileFromResource(context, R.raw.simple_vertex_shader)
        val fragmentShaderSource = readTextFileFromResource(context, R.raw.simple_fragment_shader)
        //创建顶点着色器和片段着色器
        val vertexShader = compileVertexShader(vertexShaderSource)
        val fragmentShader = compileFragmentShader(fragmentShaderSource)
        //链接顶点着色器和片段着色器到Program上
        val program = linkProgram(vertexShader, fragmentShader)
        //验证Program
        validateProgram(program)
        //使用Program
        glUseProgram(program)
        //获取uniform的位置存入uColorLocation
        uColorLocation = glGetUniformLocation(program, U_COLOR)
        //获取属性的位置
        aPositionLocation = glGetAttribLocation(program, A_POSITION)
        //从头开始读取
        vertexData.position(0)
        //在缓冲区中找到a_Position对应的数据 属性位置 一个顶点关联多少个分量(两个，x和y) 数据类型 整型时才有效 多个属性才有效 去哪里读
        glVertexAttribPointer(aPositionLocation, 2, GL_FLOAT, false, 0, vertexData)
        //去哪里找数据
        glEnableVertexAttribArray(aPositionLocation)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
    }

    override fun onDrawFrame(gl: GL10?) {
        glClear(GL_COLOR_BUFFER_BIT)
        //绘制三角形
        //更新u_Color，因为是vec4,因此要四个值，指定颜色
        glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f)
        //绘制什么形状 从顶点数组的开头开始读顶点 读取6个顶点
        glDrawArrays(GL_TRIANGLES, 0, 6)

        //绘制直线
        ////更新u_Color，因为是vec4,因此要四个值，指定颜色
        glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f)
        //绘制直线 从6开始读 读取2个
        glDrawArrays(GL_LINES, 6, 2)

        //绘制两个点
        glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f)
        glDrawArrays(GL_POINTS, 8, 1)

        glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f)
        glDrawArrays(GL_POINTS, 9, 1)

        //绘制中点
        glUniform4f(uColorLocation, 0.0f, 0.0f, 0.0f, 1.0f)
        glDrawArrays(GL_POINTS, 10, 1)
    }

}