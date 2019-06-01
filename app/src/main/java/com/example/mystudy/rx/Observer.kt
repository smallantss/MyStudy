package com.example.mystudy.rx

/**
 * 观察者
 */
interface Observer<T> {

    fun onSubscribe()

    fun onNext(t:T)

    fun onError(e:Throwable)

    fun onComplete()
}