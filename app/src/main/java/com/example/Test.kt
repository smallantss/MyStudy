package com.example

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.ref.ReferenceQueue
import java.lang.ref.WeakReference
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread
import kotlin.random.Random

class Test {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val leakCanary = try {
                val leakCanaryListener = Class.forName("com.example.LeakCanary")
                val r = leakCanaryListener.getDeclaredField("INSTANCE").get(null)
                println(r)
            } catch (ignored: Throwable) {
                ignored.printStackTrace()
                println("exception:")
            }
            println("end")
        }

        private fun testMethod(method: () -> Unit) {
            method()
        }

        //虚引用对象被强引用，看能否被回收
        private fun testWeakReference() {
            val rq = ReferenceQueue<Person?>()
            val weakReference = testGc(rq)
            System.gc()
            Thread.sleep(2000)
            if (weakReference.isEnqueued) {
                //说明已经在引用队列
                println("不置null 已经被清除")
            } else {
                println("不置null 没有被清除")
            }
//            System.gc()
//            Thread.sleep(2000)
//            if (weakReference.isEnqueued){
//                //说明已经在引用队列
//                println("置null 已经被清除")
//            }else{
//                println("置null 没有被清除")
//            }
            println(weakReference.get())
        }

        private fun testGc(rq: ReferenceQueue<Person?>): WeakReference<Person?> {
            return WeakReference(Person(10), rq)
        }

        private fun testGenerate() {
            var r: Int = -1
            var threada: Thread? = null
            var threadb: Thread? = null
            var exited1 = true
            thread {
                while (true) {
                    Thread.sleep(4000)
                    r = getData()
                    println("生成:$r")
                    if (r == 4) {
                        exited1 = true
                        if (threada == null) {
                            threada = thread {
                                while (exited1) {
                                    Thread.sleep(3000)
                                    println("------" + 4)
                                }
                            }
                        }
                    } else if (r == 1) {
                        exited1 = false
                        if (threadb == null) {
                            threadb = thread {
                                while (!exited1) {
                                    Thread.sleep(1000)
                                    println("------" + 1)
                                }
                            }
                        }
                    }
                }
            }
        }

        //每5s发送一个数据
        private fun getData(): Int {
            return Random.nextInt(8)
        }

        //测试反射
        private fun testReflect() {
            //获取class
            val clazz = Person::class.java
            //获取constructor
            val constructor = clazz.getConstructor(Int::class.java)
            //获取Person对象，并传入构造函数参数
            val person = constructor.newInstance(10)
            //获取method，指定方法参数类型
            val method = clazz.getDeclaredMethod("work", String::class.java)
            //设置方法禁用检查
            method.isAccessible = true
            //获取属性
            val field = clazz.getDeclaredField("mAge")
            field.isAccessible = true
            //修改person对象的mAge值
            field.set(person, 222)
            //执行对象的方法，并传入参数
            method.invoke(person, "android")


            //获取伴生对象的class
            val com = Person::class.java
            //获取伴生对象的构造函数
            val cons = com.getDeclaredConstructor()
            //禁用检查
            cons.isAccessible = true
            //伴生对象的方法
            val method2 = com.getDeclaredMethod("eat", String::class.java)
            method2.isAccessible = true
            //执行伴生对象的方法，并传入参数
            method2.invoke(cons.newInstance(), "rice")
        }

    }
}

class Person {

    private var mAge: Int = 0

    constructor(age: Int) {
        mAge = age
    }

    @Synchronized
    fun work(name: String) {
        println("age is $mAge,work is $name")
    }

    companion object {
        fun eat(name: String) {
            println("age is $name")
        }
    }

}

data class Bread(val id: Int)

class Producer(val list: ArrayList<Bread>) {

    //生产
    fun push() {
        if (list.size >= 10) {
            println("produce max")
            return
        }
        val bread = Bread(list.size)
        list.add(bread)
        println("produce id ${list.size}")
    }

}

class Consumer(private val list: ArrayList<Bread>) {

    //消费
    fun pop() {
        if (list.isEmpty()) {
            println("list empty")
            return
        }
        println("pop 0")
        list.removeAt(0)
    }
}

fun Person.add(a: Int) {

}

object LeakCanary{

}