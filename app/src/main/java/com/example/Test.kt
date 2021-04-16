package com.example

class Test {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val list = ArrayList<Person>()
            list.add(Student())
        }

        open class Person{

        }

        class Student: Person() {

        }

    }
}