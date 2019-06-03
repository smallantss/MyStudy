package com.example.mystudy.designpattern

//工厂模式
class FactoryPattern {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val product = ConcreteFactory().create()
            product.method()

        }

    }
}

abstract class Product {
    abstract fun method()
}

class ProductA : Product() {
    override fun method() {
        System.out.println("这是ProductA")
    }
}

abstract class Factory {
    abstract fun create(): Product
}

class ConcreteFactory : Factory() {
    override fun create(): Product {
        return ProductA()
    }
}

class ReflectFactory {

    fun <T : Product> createProduct(clz: Class<T>): T {
        val instance = Class.forName(clz.name).newInstance() as T
        return instance
    }
}