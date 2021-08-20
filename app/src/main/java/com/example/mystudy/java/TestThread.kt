package com.example.mystudy.java

class TestThread {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            testByte()
        }

        private fun testByte(){
            val a: Byte = 1
            val bit1 = (a.toInt() shr 0 and 1)
            println(bit1)
        }

        private fun testLock(){
            val container = Container()
            Producer(container).start()
            Consumer(container).start()
        }
    }
}

class Consumer(private val container: Container) : Thread() {

    override fun run() {
        for (i in 0 until 40) {
            val bread = container.pop()
            println("消费:${bread.id}")
        }
    }
}

class Producer(private val container: Container) : Thread() {

    override fun run() {
        for (i in 0 until 25) {
            container.push(Bread(i))
            println("生产:$i")
        }
    }
}

class Container {

    private val lock = Object()
    private val max = 100
    private val breads = arrayOfNulls<Bread>(max)
    private var count = 0

    fun push(bread: Bread) {
        synchronized(lock) {
            //不能生产
            if (count==breads.size) {
                lock.wait()
            }
            breads[count] = bread
            count++
            lock.notifyAll()
        }
    }

    fun pop(): Bread {
        synchronized(lock) {
            //不能消费
            if (count==0) {
                lock.wait()
            }
            count--
            lock.notifyAll()
        }
        return breads[count]!!
    }
}

data class Bread(val id: Int)