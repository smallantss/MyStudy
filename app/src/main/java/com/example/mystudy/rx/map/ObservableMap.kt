package com.example.mystudy.rx.map

import com.example.mystudy.rx.Observable
import com.example.mystudy.rx.Observer

class ObservableMap<T, R>(val source: Observable<T>, val function: Function<T, R>) : Observable<R>() {

    override fun subscribeActual(observer: Observer<R>) {
        val mapObserver = MapObserver(observer, function)
        source.subscribe(mapObserver)
    }

    //静态代理，所以里面的方法都要给实际类ObservableJust去实现
    class MapObserver<T, R>(val source: Observer<R>, val function: Function<T, R>) : Observer<T> {
        override fun onSubscribe() {
            source.onSubscribe()
        }

        override fun onNext(t: T) {
            try {
                val result = function.apply(t)
                source.onNext(result)
            }catch (e:Exception){
                source.onError(e)
            }
        }

        override fun onError(e: Throwable) {
            source.onError(e)
        }

        override fun onComplete() {
            source.onComplete()
        }

    }
}