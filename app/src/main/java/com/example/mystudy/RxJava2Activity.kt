package com.example.mystudy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.mystudy.rx.Observable
import com.example.mystudy.rx.Observer
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_rx_java2.*

class RxJava2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rx_java2)

        //Observable被观察者
        //Observer 观察者
        io.reactivex.Observable.just("aaa")
                .subscribe(object : io.reactivex.Observer<String> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: String) {
                    }

                    override fun onError(e: Throwable) {
                    }


                })

        io.reactivex.Observable.create<String> {
            it.onNext(Thread.currentThread().name)
            it.onNext("aaa")
            it.onComplete()
        }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : io.reactivex.Observer<String> {
                    override fun onSubscribe(d: Disposable) {
                        L("onSubscribe")
                    }

                    override fun onNext(t: String) {
                        L("onNext->$t")
                        textView.text = "发送的线程名->$t"
                    }

                    override fun onError(e: Throwable) {
                        L("onError->")
                    }

                    override fun onComplete() {
                        textView2.text = "接收的线程名->${Thread.currentThread().name}"
                        L("onComplete->${Thread.currentThread().name}")
                    }

                })

//        Observable.just("aaa")
//                .subscribe(object : Observer<String> {
//
//                    override fun onSubscribe() {
//                        L("onSubscribe")
//                    }
//
//                    override fun onNext(t: String) {
//                        L("onNext->$t")
//                    }
//
//                    override fun onError(e: Throwable) {
//                        L("onError->")
//                    }
//
//                    override fun onComplete() {
//
//                        L("onComplete->${Thread.currentThread().name}")
//                    }
//
//                })


    }

    fun L(s: String) {
        Log.e("TAG", s)
    }
}
