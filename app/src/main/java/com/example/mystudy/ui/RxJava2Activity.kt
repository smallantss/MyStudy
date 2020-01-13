package com.example.mystudy.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mystudy.R
import com.example.mystudy.utils.LogUtils
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function
import java.util.concurrent.TimeUnit

class RxJava2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rx_java2)

//        test()
        testOrder()

    }

    /**
     * 先登录，再获取用户信息
     */
    @SuppressLint("CheckResult")
    private fun testOrder() {
        val observable1 = Observable.timer(2, TimeUnit.SECONDS).map {
            "2s,"
        }
        observable1
                .flatMap {it1->
                    LogUtils.e(it1)
                    Observable.timer(5, TimeUnit.SECONDS).map {
                        it1.plus("5s")
                    }
                }.subscribe {
                    LogUtils.e(it)
                }
    }

    /**
     * 同时获取多个接口，结果统一处理
     */
    @SuppressLint("CheckResult")
    private fun testZip() {
        val observable1 = Observable.timer(2, TimeUnit.SECONDS).map {
            "2s,"
        }
        val observable2 = Observable.timer(5, TimeUnit.SECONDS).map {
            "5s"
        }
        observable1.subscribe {
            LogUtils.e(it)
        }
        observable2.subscribe {
            LogUtils.e(it)
        }
        Observable.zip(observable1, observable2,
                object : BiFunction<String, String, String> {
                    override fun apply(t1: String, t2: String): String {
                        return t1.plus(t2)
                    }
                })
                .subscribe {
                    LogUtils.e(it)
                }
    }

    fun test() {
        io.reactivex.Observable.create<Int> {
            it.onNext(111)
            it.onComplete()
        }.map(object : Function<Int, String> {
            override fun apply(t: Int): String {
                L("apply->${Thread.currentThread().name}")
                return t.toString()
            }
        })
                .subscribeOn(io.reactivex.schedulers.Schedulers.newThread())
                .observeOn(io.reactivex.schedulers.Schedulers.computation())
                .subscribe(object : Observer<String> {
                    override fun onSubscribe(d: Disposable) {
                        L("onSubscribe->${Thread.currentThread().name}")
                    }

                    override fun onNext(t: String) {
                        L("onNext->${Thread.currentThread().name}")
                    }

                    override fun onError(e: Throwable) {
                        L("onError->${Thread.currentThread().name}")
                    }

                    override fun onComplete() {
                        L("onComplete->${Thread.currentThread().name}")
                    }
                })


    }

    fun L(s: String) {
        Log.e("TAG", s)
//        val stafs = StatFs("")
//        stafs.availableBlocks
    }
}
