package com.example.mystudy

import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.cos

class Struct {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            shortPath()
        }

        fun shortPath(){
            val end = Node("end")
            val a = Node("a", mapOf(end to 1))
            val b = Node("b", mapOf(a to 3,end to 5))
            val start = Node("start", mapOf(a to 6,b to 2))
            val graph = HashMap<String,Map<String,Int>>().apply {
                this["start"] = mapOf("a" to 6,"b" to 2)
                this["a"] = mapOf("fin" to 1)
                this["b"] = mapOf("a" to 3,"c" to 1,"fin" to 5)
                this["c"] = mapOf("fin" to 4)
            }
            //开销表
            val costs = HashMap<String,Int>().apply {
                this["a"] = 6
                this["b"] = 2
                this["c"] = Int.MAX_VALUE
                this["fin"] = Int.MAX_VALUE
            }
            //存储父节点
            val parent = HashMap<String,String>().apply {
                this["a"] = "start"
                this["b"] = "start"
//                this["c"] = "b"
                this["fin"] = "None"
            }
            //记录已经处理过的
            val process = ArrayList<String>()
            try {
                //首先找出开销最小的节点
                var min = findLowest(costs,process)
                while (min != "None"){
                    val cost = costs[min]!!
                    val minMap = graph[min]
                    minMap!!.keys.forEach {
                        val newCost = cost + minMap[it]!!
                        if (costs[it]!!>newCost){
                            costs[it] = newCost
                            parent[it] = min
                            println("final is $min")
                        }
                    }
                    process.add(min)
                    min = findLowest(costs,process)
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }

        fun findLowest(costs:HashMap<String,Int>,data:List<String>):String{
            var lowest = Int.MAX_VALUE
            var lowestNode = "None"
            var cost:Int
            costs.keys.forEach {
                cost = costs[it]!!
                if (cost<lowest && !data.contains(it)){
                    lowest = cost
                    lowestNode = it
                }
            }
            return lowestNode
        }

        /**
         * 创建一个队列
         * 取出第一个元素，判断是否满足要求，不满足的话将该元素子元素添加到队列后
         * 取出第二个元素，依次按照上述操作
         * 直到满足条件为止
         */
        fun firstSearch() {
            val queue = generateData()
            while (queue.isNotEmpty()) {
                val ele = queue.pop()
                if (ele.name.contains("sss")) {
                    println("final resule is $ele")
                    return
                } else {
                    queue.addAll(ele.friends)
                }
            }
        }

        fun generateData() = ArrayDeque<Me>().apply {
            push(
                Me(
                    "a",
                    listOf(
                        Me("aa", listOf(Me("aaa"))),
                        Me("ab", listOf(Me("abb"))),
                        Me("ac", listOf(Me("acc")))
                    )
                )
            )
            push(
                Me(
                    "b",
                    listOf(
                        Me("ba", listOf(Me("sss"))),
                        Me("bb", listOf(Me("bbb"))),
                        Me("bc", listOf(Me("bcc")))
                    )
                )
            )
        }
    }
}

data class Me(
    val name: String,
    val friends: List<Me> = emptyList()
)

data class Node(val name:String,val chain:Map<Node,Int>?=null)
