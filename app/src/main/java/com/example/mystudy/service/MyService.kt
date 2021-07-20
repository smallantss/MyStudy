package com.example.mystudy.service

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity

class TestService : Service() {

    class TestBinder(val service: TestService) : Binder() {}

    override fun onBind(intent: Intent?): IBinder? {
        return TestBinder(this)
    }

    fun doSomething() {

    }

    fun setCallback(method: () -> Unit) {
        method.invoke()
    }
}

class MyActivity : AppCompatActivity() {

    class MyConn(val callback: () -> Unit) : ServiceConnection {

        private var testBinder: TestService.TestBinder? = null

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            testBinder = service!! as TestService.TestBinder
            //获取Service实例调用Service的方法
            testBinder?.service?.doSomething()
            testBinder?.service?.setCallback {
                callback.invoke()
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            testBinder = null
        }
    }

    fun updateUi() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, TestService::class.java)
        val conn = MyConn {
            updateUi()
        }
        bindService(intent, conn, Context.BIND_AUTO_CREATE)
    }

}