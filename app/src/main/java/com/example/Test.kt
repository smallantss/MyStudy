package com.example

import com.example.mystudy.java.loader.DiskClassLoader
import com.example.mystudy.map.Node
import com.example.opengl.opencv.OpenCvUtil
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import java.io.DataInputStream
import java.io.PrintWriter
import java.lang.Exception
import java.lang.ref.ReferenceQueue
import java.lang.ref.WeakReference
import java.net.*
import java.nio.charset.Charset
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
        fun main(args: Array<String>) = runBlocking {
            linkReverse()
        }

        private fun linkReverse() {
            val head = Node().apply {
                value = -1
            }
            var last = head
            for (i in 0..3) {
                var temp: Node = Node().apply {
                    value = i
                }
                last.next = temp
                last = temp
            }
            logLink(head)

            reverseLink2(head)
        }

        private fun reverseLink2(node: Node?) {
            var newNode = Node()
            var head = node
            while (head!=null){
                val next = head.next
                head.next = newNode.next
                newNode.next = head
                head = next
            }
            logLink(newNode.next)
        }

        private fun reverseLink(node: Node?) {
            var before: Node? = null
            var mid: Node? = node
            var after: Node? = node?.next
            mid?.next = before
            after?.next = mid
            println("before:${before?.value},mid:${mid?.value},after:${after?.value}")
            //-1 0 1 2 3
            while (after != null) {
                before = mid
                mid = after
                after = after.next
                mid.next = before
                after.next = mid
                println("before:${before?.value},mid:${mid?.value},after:${after?.value}")
            }
        }

        private fun logLink(node: Node?) {
            var temp = node
            while (temp != null) {
                println(temp.value)
                temp = temp.next
            }
            println("end log link")
        }

        private suspend fun studyStateFlow() {
            val p = Person(10)
            val stateFlow = MutableStateFlow(Person(10))
            stateFlow.emit(Person(10))
            stateFlow.emit(Person(11))
            stateFlow.collect {
                println(it.age)
            }
        }

        private suspend fun studyFlowMap() {
            val scope = CoroutineScope(Dispatchers.IO)
            val flow = flow {
                emit(1)
                //共享开始协程作用域范围，控制共享的开始和结束的策略， 状态流的重播个数
            }.shareIn(scope, WhileSubscribed(500), 0)
        }

        private suspend fun studySharedFlow() {
            val sharedFlow = MutableSharedFlow<String>(2)
            sharedFlow.emit("collect before 1")
            sharedFlow.emit("collect before 2")
            sharedFlow.emit("collect before 3")
            CoroutineScope(Dispatchers.IO).launch {
                delay(1000)
                sharedFlow.emit("collect after 1")
                sharedFlow.emit("collect after 2")
            }
            sharedFlow.collect {
                println(it)
            }
        }

        private suspend fun studySimpleFlow() {
            flow {
                println("aaa")
                while (currentCoroutineContext().isActive) {
                    emit(10)
                    println("bbb")
                }
            }.flowOn(Dispatchers.IO).collect {

            }
        }

        private suspend fun testFlow() {
            val person = Person(10)
            val stateFlow = MutableStateFlow(person)
            CoroutineScope(Dispatchers.IO).launch {
                while (true) {
                    stateFlow.value = Person(20)
                    delay(1000)
                }
            }
            stateFlow.collect {
                println(it)
            }
        }

        private fun testSocket() {
            initSocketServer()
            initSocketClient()
        }

        private fun initSocketClient() {
            thread {
                val socket = Socket("localhost", 1234)
                val os = socket.getOutputStream()
                val data = "this is client data\n"
                os.write(data.toByteArray())
                println("client send server data:$data")
            }
        }

        private fun initSocketServer() {
            thread {
                val server = ServerSocket(1234)
                val socket: Socket = server.accept()
                val data = ByteArray(1024)
                socket.getInputStream().read(data)
                println("server get client data:${data.toString(Charset.defaultCharset())}")
            }
        }

        private fun testRead() {
            val socket = Socket("localhost", 1234)
            socket.getOutputStream().write("".toByteArray())
            socket.getInputStream().readBytes()
            val data = ByteArray(1024)
            socket.getInputStream().read(data)
        }

        private fun testUDP() {
            initUDPServer()
            initUDPClient()
        }

        private fun initUDPServer() {
            val server = DatagramSocket(1234)
            val data = ByteArray(200)
            val pakData = DatagramPacket(data, data.size)
            thread {
                while (true) {
                    server.receive(pakData)
                    val r = data.toString(Charsets.UTF_8)
                    println("server get client data:$r")
                    Thread.sleep(1000)
                }
            }
            thread {
                while (true) {
                    if (pakData.address != null) {
                        val r = "this is server data"
                        server.send(DatagramPacket(r.toByteArray(), r.toByteArray().size, pakData.address, pakData.port))
                        println("server send client data:${r}")
                    }
                    Thread.sleep(1000)
                }
            }
        }

        private fun initUDPClient() {
            val client = DatagramSocket()
            thread {
                while (true) {
                    val data = ("this is client data").toByteArray()
                    val pakData = DatagramPacket(data, 0, data.size, InetAddress.getByName("172.16.1.163"), 1234)
                    client.send(pakData)
                    println("client send server data:this is client data")
                    Thread.sleep(1000)
                }
            }
            thread {
                while (true) {
                    val data = ByteArray(200)
                    val pakData = DatagramPacket(data, 0, data.size)
                    client.receive(pakData)
                    val r = data.toString(Charsets.UTF_8)
                    println("client get server data:$r")
                    Thread.sleep(1000)
                }
            }
        }

        private fun testCancel() {
            val job = CoroutineScope(Dispatchers.IO).launch {
                val innerJob = launch {
                    while (true) {
                        delay(1000)
                        println("inner job")
                    }
                }
                while (true) {
                    delay(1000)
                    println("job")
                }
            }
            Thread.sleep(5000)
            job.cancel()
        }

        private fun testByte() {
            //Byte 8位有符号整数
            //0x11111111 最大值127，前面一位代表符号
            val a: Byte = 255.toByte()
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

class Person(var age: Int) {

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