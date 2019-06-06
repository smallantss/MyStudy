package com.example.mystudy.designpattern

/**
 * 抽象工厂模式
 */
class AbstractFactoryPattern {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val factory1 = ConcreteFactory1()
            factory1.createAbstractProductA().method()
            val factory2 = ConcreteFactory2()
            factory2.createAbstractProductB().method()

            println("NormalFactory:")
            val normalFactory = NormalFactory()
            normalFactory.createTire().create()
            normalFactory.createEngine().create()

            println("HighFactory:")
            val highFactory = HighFactory()
            highFactory.createTire().create()
            highFactory.createEngine().create()
        }
    }
}

//抽象的产品A
abstract class AbstractProductA{

    abstract fun method()
}

//抽象的产品B
abstract class AbstractProductB{

    abstract fun method()
}

//具体的的产品A的实现类
class ConcreteProductA1: AbstractProductA() {
    override fun method() {
        System.out.println("A的具体产品A1")
    }
}

class ConcreteProductA2: AbstractProductA() {
    override fun method() {
        System.out.println("A的具体产品A2")
    }
}

//具体的产品B的实现类
class ConcreteProductB1:AbstractProductB(){
    override fun method() {
        System.out.println("B的具体产品B1")
    }

}

class ConcreteProductB2:AbstractProductB(){
    override fun method() {
        System.out.println("B的具体产品B2")
    }

}

//抽象工厂
abstract class AbstractFactory{
    //抽象方法获取抽象产品A
    abstract fun createAbstractProductA():AbstractProductA
    //抽象方法获取抽象产品B
    abstract fun createAbstractProductB():AbstractProductB
}

//具体工厂产品具体的产品
class ConcreteFactory1:AbstractFactory(){
    override fun createAbstractProductA(): AbstractProductA {
        return ConcreteProductA1()
    }

    override fun createAbstractProductB(): AbstractProductB {
        return ConcreteProductB1()
    }
}

class ConcreteFactory2:AbstractFactory(){
    override fun createAbstractProductA(): AbstractProductA {
        return ConcreteProductA2()
    }

    override fun createAbstractProductB(): AbstractProductB {
        return ConcreteProductB2()
    }
}

//抽象汽车工厂
abstract class CarFactory{
    //抽象轮胎
    abstract fun createTire():ITire
    //抽象引擎
    abstract fun createEngine():IEngine
}
//抽象产品
interface ITire{
    fun create()
}

interface IEngine{
    fun create()
}
//具体产品
class NormalTire:ITire{
    override fun create() {
        System.out.println("生产普通轮胎")
    }
}

class HighTire:ITire{
    override fun create() {
        System.out.println("生产高端轮胎")
    }
}

class NormalEngine:IEngine{
    override fun create() {
        System.out.println("生产普通引擎")
    }
}

class HighEngine:IEngine{
    override fun create() {
        System.out.println("生产高端引擎")
    }
}

//具体工厂
class NormalFactory: CarFactory() {
    override fun createTire(): ITire {
        return NormalTire()
    }
    override fun createEngine(): IEngine {
        return NormalEngine()
    }
}
class HighFactory: CarFactory() {
    override fun createTire(): ITire {
        return HighTire()
    }
    override fun createEngine(): IEngine {
        return HighEngine()
    }
}