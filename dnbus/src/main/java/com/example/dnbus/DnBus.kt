package com.example.dnbus

import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

class DnBus private constructor(){


    //装载订阅方法的map  key 是组件 value是方法集合
    private var map :HashMap<Any,List<MethodManager>>

    private var executorService:ExecutorService?=null

    init {
        map = HashMap()
        executorService = Executors.newCachedThreadPool()
    }

    companion object {
        var mDnBus:DnBus?=null

        fun getInstance():DnBus{
            if (mDnBus==null){
                synchronized(DnBus::class.java){
                    if (mDnBus==null){
                        mDnBus = DnBus()
                    }
                }
            }
            return mDnBus!!
        }
    }

    fun register(obj: Any) {
        var list = map[obj]
        if (list==null){
            list = findAnnotationsMethod(obj)
            map[obj] = list
        }
    }

    //反射寻找activity中方法
    private fun findAnnotationsMethod(obj: Any): List<MethodManager> {
        val methodManagerList = ArrayList<MethodManager>()
        val clazz = obj.javaClass
        val methods = clazz.declaredMethods
        methods.forEach {
            val annotations = it.getAnnotation(Subscribe::class.java)


//            获取参数的返回类
//            if (it.genericReturnType.toString()!="void"){
//
//            }
//            获取参数的类型 方法有几个参数
            val parameterTypes = it.parameterTypes
            if (parameterTypes.size!=1){

            }
            if (annotations!=null){
                val methodManager = MethodManager(parameterTypes[0],annotations.threadMode,it)
                methodManagerList.add(methodManager)
            }
        }

        return methodManagerList
    }

    fun post(obj:Any){
        val keys = map.keys
        keys.forEach{key->
            val list = map[key]
            if (list!=null&& list.isNotEmpty()){
                //便利订阅者，判断参数类型是否和发布者类型一致
                list.forEach {
                    if (it.clazz.isAssignableFrom(obj::class.java)){
                        invoke(it,key,obj)
                    }
                }
            }
        }
    }

    private fun invoke(method: MethodManager, obj: Any, setter: Any) {
        val method1 = method.method
        method1.invoke(obj,setter)
    }
}