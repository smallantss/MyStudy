package com.example.mystudy.designpattern

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

/**
 * 代理模式
 */
class ProxyPattern {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val person = Person()
//            val lawyer = Lawyer(person)
//            lawyer.submit()
//            lawyer.finish()

            //构造一个动态代理
            val proxy = DynamicHandler(person)
            //获取被代理类的classLoader
            val loader = person.javaClass.classLoader
            //动态构造一个律师 ILawSuit
            val lawyer = Proxy.newProxyInstance(loader, arrayOf(ILawSuit::class.java), proxy) as ILawSuit
            lawyer.submit()
            lawyer.finish()
        }
    }

}

interface ILawSuit {

    fun submit()

    fun finish()
}

class Person : ILawSuit {
    override fun submit() {
        println("Person submit")
    }

    override fun finish() {
        println("Person finish")
    }

}

class Lawyer(val lawSuit: ILawSuit) : ILawSuit {

    override fun submit() {
        lawSuit.submit()
    }

    override fun finish() {
        lawSuit.finish()
    }

}

class DynamicHandler(val obj: Any) : InvocationHandler {

    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
        val result = method?.invoke(obj, *args.orEmpty())
        return result
    }

}