package com.example.mystudy.rx

import com.example.mystudy.rx.map.Function
import com.example.mystudy.rx.map.ObservableMap


/**
 * 被观察者
 */
abstract class Observable<T> : ObservableSource<T> {

    abstract fun subscribeActual(observer: Observer<T>)

    companion object {

        //方法泛型
        fun <T> just(item: T): Observable<T> {
            return onAssembly(ObservableJust(item))
        }

        private fun <T> onAssembly(source: Observable<T>): Observable<T> {
            return source
        }
    }

    override fun subscribe(observer: Observer<T>) {
        subscribeActual(observer)
    }

    fun subscribe(onNext: Consumer<T>) {
        //可以认为是个静态代理
        val observer = LambdaObserver(onNext)
        subscribe(observer)
    }

    //这里不能再瞎几把乱声明为 <T,R>了，会认为又声明了泛型
    fun <R> map(function: Function<T, R>): Observable<R> {
        return onAssembly(ObservableMap(this, function))
    }

}