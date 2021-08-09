package com.example

import kotlinx.coroutines.*
import okhttp3.*
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.ServerSocket
import java.net.Socket
import java.util.*
import java.util.concurrent.CountDownLatch
import kotlin.concurrent.thread

interface Worker {
    fun work()
}

class JavaWorker : Worker {
    override fun work() {
        println("java work")
    }
}

class Manager(var staff: Worker) : Worker by staff {
}

inline fun multiplyByTwo(num: Int): Int {
    return num * 2
}

class Test {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            testSocket()
        }

        private fun testSocket() {
            initServer()
            initClient()
        }

        private fun initServer() {
            val server = ServerSocket(1234)
            var socket:Socket?=null
            thread {
                socket = server.accept()
                while (true) {
                    val data = DataInputStream(socket!!.getInputStream()).readUTF()
                    println("---2---server get client data:$data")
                    Thread.sleep(1000)
                }
            }
            thread {
                while (true) {
                    if (socket!=null){
                        DataOutputStream(socket!!.getOutputStream()).writeUTF("this is server data")
                        println("---a---server send data :this is server data")
                    }
                    Thread.sleep(1500)
                }
            }
        }

        private fun initClient() {
            val client = Socket("localhost", 1234)
            thread {
                while (true) {
                    DataOutputStream(client.getOutputStream()).writeUTF("this is client data")
                    println("---1---client send data :this is client data")
                    Thread.sleep(1000)
                }
            }
            thread {
                while (true) {
                    val data = DataInputStream(client.getInputStream()).readUTF()
                    println("---b---client get server data:$data")
                    Thread.sleep(1000)
                }
            }
        }

        private fun testThreadLocal() {
            val countDownLatch = CountDownLatch(2)
            thread {
                Thread.sleep(1000)
                println("countDown 1 before")
                countDownLatch.countDown()
                println("countDown 1")
            }
            thread {
                Thread.sleep(500)
                println("countDown 2 before")
                countDownLatch.countDown()
                println("countDown 2")
            }
            countDownLatch.await()
            println("end")
        }

        private fun testRandom() {
            val random = Random()
            for (a in 0 until 20) {
                val i = random.nextInt(5)
                println(i)
            }
        }

        //协程
        private fun testCoroutine() {
            runBlocking {
                println("runBlocking is start")
                launch {
                    println("Task from launch start")
                    delay(2000L)
                    println("Task from launch end")
                }
                coroutineScope {
                    println("Task from coroutineScope start")
                    launch {
                        println("Task from coroutineScope launch start")
                        delay(500L)
                        println("Task from coroutineScope launch end")
                    }
                    delay(2000L)
                    println("Task from coroutineScope end")
                }
                println("runBlocking is over")
            }
        }

        private fun testAsync() {
            runBlocking {
                val one = async {
                    demoOne()
                }
                val two = async {
                    demoTwo()
                }
                //AAAAAAAAAAAAAAAAAAAAAA
                println("The answer is ${one.await() + two.await()}")
            }
        }

        private suspend fun demoOne(): Int {
            delay(1000L)
            return 1
        }

        private suspend fun demoTwo(): Int {
            delay(1000L)
            return 2
        }

    }

}

