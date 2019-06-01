package com.example.mystudy

import android.app.IntentService
import android.content.Intent

class MyIntentService(name: String?) : IntentService(name) {

    override fun onHandleIntent(intent: Intent?) {
        //另外一个线程中处理任务
    }
}