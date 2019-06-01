package com.example.mystudy.rx


/**
 * 被观察者
 */
abstract class Observable<T> :ObservableSource<T>{

    companion object {

        //方法泛型
        fun <T> just(item:T):ObservableSource<T>{
            return onAssembly(ObservableJust(item))
        }

        private fun <T> onAssembly(source: ObservableJust<T>): ObservableSource<T> {
            return source
        }
    }

    override fun subscribe(observer: Observer<T>) {
        subscribeActual(observer)
    }

    abstract fun subscribeActual(observer: Observer<T>)





}