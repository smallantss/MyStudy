package com.example.mystudy.designpattern

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

class ProxyPattern {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val man = Man("XWY")

            //返回的是一个实现了目标接口IBank的实例对象，是java创建的
            val iBank = Proxy.newProxyInstance(
                    //类的加载器
                    IBank::class.java.classLoader,
                    //目标接口（必须为接口）
                    arrayOf(IBank::class.java),
                    object : InvocationHandler {
                        override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any {
                            //目标接口调用的所有方法都会走到这里
                            System.out.println("Proxy----------" + method?.name)
                            //调用被代理对象man的方法如applyCard
                            val resultObject = method?.invoke(man, *args.orEmpty())
                            return resultObject ?: Any()
                        }
                    }) as IBank
            //调用这个方法会走到上面invoke方法
            iBank.applyCard()
            iBank.lostCard()
        }
    }
}

interface IBank {
    fun applyCard()

    fun lostCard()
}

//被代理对象
class Man(val name: String) : IBank {
    override fun lostCard() {
        System.out.println("$name is lost card")
    }

    override fun applyCard() {
        System.out.println("$name is apply card")
    }
}

//代理对象
class Worker(val iBank: IBank) : IBank {
    override fun lostCard() {

    }

    override fun applyCard() {
        //...
        iBank.applyCard()
    }

}