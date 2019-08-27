package com.example.mystudy.designpattern

import android.util.Log

/**
 * 观察者模式：
 * 抽象被观察者 Observable
 * 被观察者
 * 抽象观察者 Observer
 * 观察者
 */
class ObserverPattern {


    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val observer1 = ConcreteObserver1()
            val observer2 = ConcreteObserver2()
            val observer3 = ConcreteObserver3()

            val observable = ConcreteObservable()
            observable.addObserver(observer1)
            observable.addObserver(observer2)
            observable.addObserver(observer3)

            observable.doSomething(5)
        }


    }

}

//抽象被观察者 增删观察者
interface Observable {

    fun addObserver(observer: Observer)

    fun removeObserver(observer: Observer)

}

class ConcreteObservable : Observable {

    val observerList = ArrayList<Observer>()

    override fun addObserver(observer: Observer) {
        observerList.add(observer)

    }


    override fun removeObserver(observer: Observer) {
        observerList.remove(observer)
    }


    fun doSomething(tag: Int) {
        observerList.forEach {
            it.update(tag)
        }
    }

}

//抽象观察者
interface Observer {

    fun update(tag: Int)

}

class ConcreteObserver1 : Observer {

    var state = 10

    override fun update(tag: Int) {
        state++
        println("ConcreteObserver1收到通知$tag，状态->$state")
    }

}

class ConcreteObserver2 : Observer {

    var state = 20

    override fun update(tag: Int) {
        state++
        println("ConcreteObserver2收到通知$tag，状态->$state")
    }

}

class ConcreteObserver3 : Observer {

    var state = 30

    override fun update(tag: Int) {
        state++
        println("ConcreteObserver3收到通知$tag，状态->$state")
    }

}