package com.example

import java.util.*

class Test {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            testRandom()
        }

        private fun testRandom() {
            val random = Random()
            for (a in 0 until 20){
                val i = random.nextInt(5)
                println(i)
            }

        }

    }

}

