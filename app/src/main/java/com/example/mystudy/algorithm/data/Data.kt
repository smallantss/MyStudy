package com.example.mystudy.algorithm.data

import kotlin.math.max
import kotlin.math.min

class DataStruct {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
//            println(findMaxSquare(1680,640))
//            println(sum(listOf(1, 2, 3, 4, 5)))
            println(insertSort(mutableListOf(5, 2, 1, 4, 8, 3, 0)))
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
                } else if (it > temp) {
                    greaterArr.add(it)
                }
            }
            return quickSort(lessArr) + temp + quickSort(greaterArr)
        }

        /**
         * 冒泡排序
         * 相邻两个数进行比较交换，一轮找出最小值
         * 第二轮，比较数-1，继续
         */
        fun bubbleSort(arr: MutableList<Int>): List<Int> {
            for (i in 0 until arr.size - 1) {
                println("i is $i")
                for (j in 0 until arr.size - 1 - i) {
                    println("j is $j")
                    if (arr[j] > arr[j + 1]) {
                        val temp = arr[j]
                        arr[j] = arr[j + 1]
                        arr[j + 1] = temp
                    }
                }
            }
            return arr
        }

        /**
         * 选择排序
         * 找到剩余元素中最小值，与最左边数字进行交换
         */
        fun selectSort(arr: MutableList<Int>): List<Int> {
            for (i in 0 until arr.size - 1) {
                var minIndex = i
                for (j in 1 + i until arr.size) {
                    if (arr[minIndex] > arr[j]) {
                        minIndex = j
                    }
                }
                if (minIndex != i) {
                    val temp = arr[i]
                    arr[i] = arr[minIndex]
                    arr[minIndex] = temp
                }
            }
            return arr
        }

        /**
         * 插入排序
         * 右侧未排序的元素与左侧已排序的元素比较，不满足条件，依次比较下去
         */
        fun insertSort(arr: MutableList<Int>): List<Int> {
            for (i in 0 until arr.size - 1) {
                //后面的和前面的比较
                for (j in 1 + i downTo 1) {
                    if (arr[j] < arr[j - 1]) {
                        //满足条件就交换
                        var temp = arr[j]
                        arr[j] = arr[j - 1]
                        arr[j - 1] = temp
                    } else {
                        //不满足，跳出循环，因为前面都是符合条件的
                        break
                    }
                }
            }
            return arr
        }

        fun mergeSort(arr: MutableList<Int>): List<Int> {
            val divider = arr.size / 2
            val left = arr.filterIndexed { i, t ->
                i <= divider
            }
            val right = arr.filterIndexed { i, t ->
                i > divider
            }
            return arr
        }
    }
}