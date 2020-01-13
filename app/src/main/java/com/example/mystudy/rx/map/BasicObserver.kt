package com.example.mystudy.rx.map

import com.example.mystudy.rx.Observer

abstract class BasicObserver<T,R>(val downStream:Observer<T>): Observer<T> {

    override fun onSubscribe() {
        downStream.onSubscribe()
    }
}