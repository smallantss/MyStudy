package com.example

import kotlinx.coroutines.*
import java.util.*

class Test {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
//            testCoroutine()


            testAsync()
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

        private fun testAsync(){
            runBlocking {
                val one = async{
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

