package com.example

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

        open class Person{

        }

        class Student: Person() {

        }

    }
}