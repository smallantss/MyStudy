package com.example.mystudy.mvvm

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import android.util.Log

/**
 * 是一个LifecycleObserver观察者，观察LifecycleOwner的生命周期
 */
class LocationListener:LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun initialize(){
        l("initialize")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun startLocation() {
        l("startLocation")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun pauseLocation() {
        l("pauseLocation")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun release(){
        l("release")
    }

    fun l(s: String) {
        Log.e("TAG", s)
    }

}