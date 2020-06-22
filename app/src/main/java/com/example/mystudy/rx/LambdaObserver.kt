package com.example.mystudy.rx

class LambdaObserver<T>(val consumer: Consumer<T>) :Observer<T> {
    override fun onSubscribe() {

    }

    override fun onNext(t: T) {
        consumer.apply(t)
    }

    override fun onError(e: Throwable) {
    }

    override fun onComplete() {
    }
}