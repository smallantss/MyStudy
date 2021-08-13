package com.example.opengl

import android.opengl.GLES20.*
import com.example.mystudy.loge

fun compileVertexShader(shaderCode: String): Int {
    return compileShader(GL_VERTEX_SHADER, shaderCode)
}

fun compileFragmentShader(shaderCode: String): Int {
    return compileShader(GL_FRAGMENT_SHADER, shaderCode)
}

//编译着色器代码
fun compileShader(type: Int, shaderCode: String): Int {
    //创建一个着色器对象并返回ID
    val shaderObjectId = glCreateShader(type)
    if (shaderObjectId == 0) {
        //0代表失败
        loge("could not create new shader")
        return 0
    }
    //读取shaderCode的源码,并和shaderObjectId所引用的着色器对象关联
    glShaderSource(shaderObjectId, shaderCode)
    //最后编译着色器
    glCompileShader(shaderObjectId)
    val compileStatus = IntArray(1)
    //检查编译状态是否成功，结果放到int数组的第0个位置
    glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0)
    if (compileStatus[0] == 0) {
        //失败
        loge("result of compile source \n $shaderCode \n ${glGetShaderInfoLog(shaderObjectId)}")
        glDeleteShader(shaderObjectId)
        return 0
    }
    return shaderObjectId
}

//将顶点着色器和片段着色器进行连接，可以在多个程序中使用同一个着色器
fun linkProgram(vertexShaderId: Int, fragmentShaderId: Int): Int {
    //创建Program
    val programObjectId = glCreateProgram()
    if (programObjectId == 0) {
        loge("could not create new program")
        return 0
    }
    //将顶点着色器和片段着色器链接到Program上
    glAttachShader(programObjectId, vertexShaderId)
    glAttachShader(programObjectId, fragmentShaderId)
    glLinkProgram(programObjectId)
    val linkStatus = IntArray(1)
    //获取链接状态
    glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0)
    if (linkStatus[0] == 0) {
        loge("result of linking program \n ${glGetProgramInfoLog(programObjectId)}")
        glDeleteProgram(programObjectId)
        loge("link program failed")
        return 0
    }
    return programObjectId
}

//验证这个是否是有效的
fun validateProgram(programObjectId: Int): Boolean {
    glValidateProgram(programObjectId)
    val validateStatus = IntArray(1)
    glGetProgramiv(programObjectId, GL_VALIDATE_STATUS, validateStatus, 0)
    loge("result of validate program ${validateStatus[0]},\n log:${glGetProgramInfoLog(programObjectId)}")
    return validateStatus[0] != 0
}