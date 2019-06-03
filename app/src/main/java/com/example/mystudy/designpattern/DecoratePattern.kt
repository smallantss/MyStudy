package com.example.mystudy.designpattern


class DecoratePattern {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            testDecorate()
        }

        private fun testDecorate() {
            val coffee = Coffee(5.0)
            val sugar = Sugar(coffee)
            println(sugar.info() + "---" + sugar.cost())
            val milk = Milk(sugar)
            println(milk.info() + "---" + milk.cost())
        }
    }

}

//抽象组件 如：可以喝的东西
internal interface Drink {
    //花费
    fun cost(): Double

    //信息
    fun info(): String
}

//具体组件 如：咖啡是喝的
internal class Coffee(private val money: Double) : Drink {

    override fun cost(): Double {
        return money
    }

    override fun info(): String {
        return "咖啡的价格是$money"
    }
}

//抽象装饰类 包含对抽象组件的引用，装饰共有的方法 如：可以给咖啡加东西
internal abstract class Decorate(private val drink: Drink) : Drink {

    override fun cost(): Double {
        return drink.cost()
    }

    override fun info(): String {
        return drink.info()
    }
}

//具体装饰类  装饰具体组件 如：咖啡可以加糖，牛奶
internal class Sugar(drink: Drink) : Decorate(drink) {

    override fun cost(): Double {
        return super.cost() + 2
    }

    override fun info(): String {
        return super.info() + "加入了Sugar"
    }
}

internal class Milk(drink: Drink) : Decorate(drink) {

    override fun cost(): Double {
        return super.cost() + 3
    }

    override fun info(): String {
        return super.info() + "加入了Milk"
    }
}