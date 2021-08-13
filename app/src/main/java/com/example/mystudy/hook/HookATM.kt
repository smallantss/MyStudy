package com.example.mystudy.hook

import android.content.Intent
import com.example.mystudy.loge
import java.lang.Exception
import java.lang.reflect.Field
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

object HookATM {

    fun hook() {
        try {
            //找到IActivityTaskManagerSingleton
            val clazz = Class.forName("android.app.ActivityTaskManager")
            val singleton = getField(clazz, null, "IActivityTaskManagerSingleton")
            //找到Singleton的mInstance属性
            val singletonClazz = Class.forName("android.util.Singleton")
            val instanceField = getField(singletonClazz, "mInstance")
            //获取IActivityTaskManagerSingleton的mInstance属性，即实例对象
            val activityTaskManager = instanceField.get(singleton)
            //获取IActivityTaskManager，创建动态代理
            val iclazz = Class.forName("android.app.IActivityTaskManager")
            val proxy = Proxy.newProxyInstance(Thread.currentThread().contextClassLoader, arrayOf(iclazz), IActivityManagerProxy(activityTaskManager))
            instanceField.set(singleton, proxy)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun getField(clazz: Class<*>, target: Any?, name: String): Any {
        val field: Field = clazz.getDeclaredField(name)
        field.isAccessible = true
        return field.get(target)
    }

    fun getField(clazz: Class<*>, name: String): Field {
        val field: Field = clazz.getDeclaredField(name)
        field.isAccessible = true
        return field
    }

    fun setField(clazz: Class<*>, target: Any, name: String, value: Any) {
        val field: Field = clazz.getDeclaredField(name)
        field.isAccessible = true
        field.set(target, value)
    }
}

class IActivityManagerProxy(private val activityTaskManager: Any) : InvocationHandler {
    override fun invoke(proxy: Any?, method: Method?, args: Array<Any>?): Any {
        if (method?.name == "startActivity") {
            loge("enter startActivity")
            var pos = -1
            var intent = Intent()
            args?.forEachIndexed { index, any ->
                if (any is Intent) {
                    pos = index
                    intent = any
                    return@forEachIndexed
                }
            }
            val subIntent = Intent()
            val packageName = "???"
            subIntent.setClassName(packageName, packageName.plus(".hook.SubActivity"))
            subIntent.putExtra("data", intent)
            args!![pos] = subIntent
        }
        return method!!.invoke(activityTaskManager, args)
    }

}