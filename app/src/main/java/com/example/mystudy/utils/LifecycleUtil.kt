package com.example.mystudy.utils

import androidx.lifecycle.*
import com.example.mystudy.loge

class LifecycleUtil : LifecycleEventObserver {

    fun create() {
        loge("create")
    }

    fun resume() {
        loge("resume")
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                loge("onStateChanged ON_CREATE")
            }
            Lifecycle.Event.ON_START -> {
                loge("onStateChanged ON_START")
            }
            Lifecycle.Event.ON_RESUME -> {
                loge("onStateChanged ON_RESUME")
            }
            Lifecycle.Event.ON_PAUSE -> {
                loge("onStateChanged ON_PAUSE")
            }
            Lifecycle.Event.ON_STOP -> {
                loge("onStateChanged ON_STOP")
            }
            Lifecycle.Event.ON_DESTROY -> {
                loge("onStateChanged ON_DESTROY")
            }
        }
    }
}