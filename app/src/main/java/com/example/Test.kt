package com.example

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread

class Test {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val a: Int = 127
            val boxedA: Int? = a
            val anotherBoxedA: Int? = a
            print(boxedA == anotherBoxedA)
            print(boxedA === anotherBoxedA)

        }

    }
}

class Person{

}

fun Person.add(a:Int){

}