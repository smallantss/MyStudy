package com.example.mystudy.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

class RvControl(app: Application) : AndroidViewModel(app) {

    val data = MutableLiveData<ArrayList<String>>()

    init {
        Log.e("TAG","当前init线程名："+Thread.currentThread().name)
        Observable.just("")
                .delay(2000,TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Log.e("TAG","当前观察线程名："+Thread.currentThread().name)
                    getData()
                }
    }

    fun getData() {
        val datas = ArrayList<String>()
        for (i in 0..49) {
            val nextInt = Random().nextInt(5)
            datas.add(when (nextInt) {
                0 -> {
                    "是"
                }
                1 -> {
                    "明天"
                }
                2 -> {
                    "辛啊啊"
                }
                3 -> {
                    "长得真帅"
                }
                4 -> {
                    "当真是这样？"
                }
                else -> ""
            } + i)
        }
        data.postValue(datas)
    }
}