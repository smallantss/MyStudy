package com.example.mystudy.rx

import android.annotation.SuppressLint
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.internal.fuseable.QueueFuseable
import io.reactivex.schedulers.Schedulers

class RxTest {

    companion object {

        @SuppressLint("CheckResult")
        @JvmStatic
        fun main(args: Array<String>) {
            val r = 6 and 4
            println(r)
            io.reactivex.Observable.just("1")
//                .delay(2, TimeUnit.SECONDS)
//                .map { t ->
//                    println("ObservableThreadName:map:" + Thread.currentThread().name)
//                    //                        throw NullPointerException()
//                    t
//                }
//                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(object : io.reactivex.Observer<String> {
                    override fun onSubscribe(d: Disposable) {
                        println("ObserverThreadName:onSubscribe:" + Thread.currentThread().name)
                    }

                    override fun onNext(t: String) {
                        println("ObserverThreadName:onNext:" + Thread.currentThread().name)
                    }

                    override fun onComplete() {
                        println("ObserverThreadName:onComplete:" + Thread.currentThread().name)
                    }

                    override fun onError(e: Throwable) {
                        println("ObserverThreadName:onError:" + Thread.currentThread().name)
                    }

                })
            Thread.sleep(3000)
        }
    }
}