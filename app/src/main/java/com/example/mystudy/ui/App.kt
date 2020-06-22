package com.example.mystudy.ui

import android.app.Application
import com.example.mystudy.aop.NetCheckUtils2

class App: Application() {

    override fun onCreate() {
        super.onCreate()
//        NetCheckUtils.init(this)
        NetCheckUtils2.getInstance().init(this)
    }

}