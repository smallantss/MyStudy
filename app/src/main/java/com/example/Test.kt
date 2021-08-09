package com.example

import com.example.mystudy.java.loader.DiskClassLoader
import com.example.opengl.opencv.OpenCvUtil
import com.google.gson.Gson
import kotlinx.coroutines.*
import java.lang.Exception
import java.lang.ref.ReferenceQueue
import java.lang.ref.WeakReference
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread
import kotlin.experimental.and
import kotlin.random.Random

open class Man(var name: String) {
    open fun work() {}
}

class Boy(name: String) : Man(name)
class Girl(name: String) : Man(name)

suspend fun add() {

}

class Test {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
//            Byte 8位有符号整数
//            0x11111111 最大值127，前面一位代表符号
           val a:Byte = 255.toByte()
            println((a.toInt() and 0xff))
            println(OpenCvUtil().testByte())
        }

        private fun testSequence() {
            val list = listOf(Boy("aaa"), Boy("bbb"), Boy("ccc"))
            val data = sequence {
                yield(1)
            }.toList()
        }

        private fun testFlatten() {
            val dataList = listOf(
                    listOf(Boy("boy1"), Boy("boy2")),
                    listOf(Girl("girl1"), Girl("girl2"))
            )
            val list = dataList.flatten()
            //单一的转换
            val r = list.flatMap {
                listOf(it.name, it.name.reversed())
            }
            println(r)
        }

        private fun testOutIn() {
            val boy: Array<Man> = arrayOf()
            val any: Array<Any> = arrayOf()
            copyFromTo(boy, any)
        }

        private fun copyFromTo(from: Array<out Man>, to: Array<in Man>) {
            from[0].name = "out 可以读取"
//            from[0] = Man("out 不能修改")
//            to[0].name = "in 不能读取"
            to[0] = Boy("in 可以修改")
            from.forEachIndexed { index, man ->
                to[index] = man
            }
        }

        private fun testInline() {
            val mans: List<Man> = listOf()
            findFirst(mans, Boy::class.java)
            findFirst2<Boy>(mans)
        }

        //找到List<Man>中的第一个实例，要么是Boy要么是Girl。
        private fun <T> findFirst(mans: List<Man>, clazz: Class<T>): T? {
            val selected = mans.filter { clazz.isInstance(it) }
            if (selected.isEmpty()) {
                return null
            }
            return clazz.cast(selected[0])
        }

        private inline fun <reified T> findFirst2(mans: List<Man>): T? {
            val selected = mans.filter { it is T }
            if (selected.isEmpty()) {
                return null
            }
            return selected[0] as T
        }

        private fun testT() {
            val boys: List<Boy> = listOf()
            testSuper(boys)
        }

        private fun testSuper(man: List<Man>) {
        }

        private fun testClassLoader() {
            val diskClassLoader = DiskClassLoader("D:\\MyStudy\\app\\src\\main\\java\\com\\example\\mystudy\\java\\loader")
            val clazz = diskClassLoader.loadClass("com.example.mystudy.java.loader.Jobs")
            val obj = clazz.newInstance()
            println(obj.javaClass.classLoader)
            val method = clazz.getDeclaredMethod("say")
            method.invoke(obj)
        }

        private suspend fun testAsync(coroutineScope: CoroutineScope) {
            println("before:" + Date())
            val a = coroutineScope.async {
                delay(3000)
                "a"
            }
            val b = coroutineScope.async {
                delay(1000)
                "b"
            }
            b.await()
            println("after:" + Date())
            a.await()
            println("finish:" + Date())
        }

        private fun testObject() {
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

class Person(age: Int) {

    private var mAge: Int = age


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

object LeakCanary {

}

data class Bean1(val name: String, val age: Int)
data class Bean2(val sex: Int, val single: Boolean)