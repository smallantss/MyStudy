package com.example.mystudy.rx

class ObservableJust<T>(val item: T) : Observable<T>() {

    override fun subscribeActual(observer: Observer<T>) {
        //代理对象 方便扩展
        val sd = ScaleDisposable(observer,item)
        observer.onSubscribe()
        sd.run()
    }

    class ScaleDisposable<T>(val observer: Observer<T>,val item: T){

        fun run(){
            try {
                observer.onNext(item)
                observer.onComplete()
            }catch (e:Exception){
                e.printStackTrace()
                observer.onError(e)
            }
        }

    }
}