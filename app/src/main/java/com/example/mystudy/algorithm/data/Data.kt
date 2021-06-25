package com.example.mystudy.algorithm.data

import kotlin.math.max
import kotlin.math.min

class DataStruct {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
//            println(findMaxSquare(1680,640))
//            println(sum(listOf(1, 2, 3, 4, 5)))
            println(quickSort(listOf(2,1,4,3,0)))
        }

        //1680*640
        //640*400
        //400*240
        //240*160
        //80*80
        fun findMaxSquare(w: Int, h: Int): Int {
            println("w:$w,h:$h")
            var tempW = w
            var tempH = h
            //400
            var temp = tempW % tempH
            if (temp == 0) {
                return tempH
            }
            return findMaxSquare(max(temp, tempH), min(temp, tempH))
        }

        fun sum(data: List<Int>): Int {
            println(data)
            if (data.size == 1) {
                return data[0]
            }
            return data[0] + sum(data.subList(1, data.size))
        }

        /**
         *快速排序
         * 从数组中选择一个元素作为基准值
         * 找出比基准值小和基准值大的元素进行分区
         * 对分区后的子数组进行快速排序
         */
        fun quickSort(arr: List<Int>): List<Int> {
            if (arr.size < 2) {
                return arr
            }
            val temp = arr[0]
            val lessArr = ArrayList<Int>()
            val greaterArr = ArrayList<Int>()
            arr.forEach {
                if (it < temp) {
                    lessArr.add(it)
                } else if (it>temp){
                    greaterArr.add(it)
                }
            }
            return quickSort(lessArr) + temp + quickSort(greaterArr)
        }
    }

}