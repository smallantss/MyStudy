package com.example.mystudy.java.loader

import java.io.File

class DiskClassLoader(private val path: String) : ClassLoader() {

    override fun findClass(name: String?): Class<*> {
        val clazz: Class<*>
        //1.加载class文件,获得class文件的字节码数组
        val clazzData: ByteArray? = loadClassData(name)
        if (clazzData == null) {
            throw ClassNotFoundException()
        } else {
            //2.将class文件字节码数组转为Class类的实例
            clazz = defineClass(name, clazzData, 0, clazzData.size)
        }
        return clazz
    }

    private fun loadClassData(name: String?): ByteArray? {
        val index = name!!.lastIndexOf('.')
        val fileName = if (index==-1){
            name.plus(".class")
        }else{
            name.substring(index+1).plus(".class")
        }
        val file = File(path, fileName)
        return file.readBytes()
    }
}