package com.example.mystudy.designpattern

class AdapterPattern {

    companion object{
        @JvmStatic
        fun main(args:Array<String>){
            val rmb = Rmb(50)
            val adapter = UsdAdapter(rmb)
            adapter.usd

        }
    }
}

class Rmb(val money:Int){
}

class UsdAdapter(val rmb: Rmb){
    val usd:Float
        get() = rmb.money/5.6f
}