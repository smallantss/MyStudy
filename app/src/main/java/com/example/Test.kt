package com.example

class Test {

    //feature_home????
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val list = listOf(0,1,2,3)
            testList(list)

        }

        fun testList(list: List<Int>){
            list.forEachIndexed { index, i ->
            }
        }
    }
}