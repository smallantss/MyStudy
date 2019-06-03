package com.example.mystudy.designpattern

class StrategyPattern {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val calculator = TrafficCalculator()
            calculator.mStrategy = BusStrategy()
            calculator.calculatePrice(20)
        }
    }
}
//实际操作类
class TrafficCalculator{
    var mStrategy:ICalculateStrategy?=null
    fun calculatePrice(km:Int){
        mStrategy?.calculate(km)
    }
}
//计算的策略
interface ICalculateStrategy{
    //具体的计算方法
    fun calculate(km:Int):String
}
//具体的算法实现
class BusStrategy:ICalculateStrategy{
    override fun calculate(km: Int):String{
        return "Bus$km"
    }
}

class CarStrategy:ICalculateStrategy{
    override fun calculate(km: Int):String{
        return "Car$km"
    }
}