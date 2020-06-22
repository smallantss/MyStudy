package com.example.mystudy.rx

import android.annotation.SuppressLint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

class RxTest {

    companion object{

        @SuppressLint("CheckResult")
        @JvmStatic
        fun main(args:Array<String>){
            Observable.just(111)
                    .map(object :com.example.mystudy.rx.map.Function<Int,String>{
                        override fun apply(t: Int): String {
                            return t.toString()
                        }
                    })
                    .map(object :com.example.mystudy.rx.map.Function<String,String>{
                        override fun apply(t: String): String {
                            return t.plus("???????")
                        }
                    })
                    .subscribe(object : Observer<String>{
                        override fun onSubscribe() {
                        }

                        override fun onNext(t: String) {

                        }

                        override fun onError(e: Throwable) {

                        }

                        override fun onComplete() {

                        }

                    })

            io.reactivex.Observable.just(1)
                    .map(object : Function<Int, String> {
                        override fun apply(t: Int): String {
                            print("ObservableThreadName:"+Thread.currentThread().name)
                            return t.toString()
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : io.reactivex.Observer<String>{
                        override fun onSubscribe(d: Disposable) {
                            print("ObserverThreadName:onSubscribe"+Thread.currentThread().name)
                        }

                        override fun onNext(t: String) {
                            print("ObserverThreadName:onNext"+Thread.currentThread().name)
                        }

                        override fun onComplete() {
                            print("ObserverThreadName:onComplete"+Thread.currentThread().name)
                        }

                        override fun onError(e: Throwable) {
                            print("ObserverThreadName:onError"+Thread.currentThread().name)
                        }

                    })
        }
    }
}