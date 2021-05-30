package com.example.mystudy.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.telecom.Call
import android.util.Log
import com.example.mystudy.R

class HandlerActivity : AppCompatActivity() {

    private var mHandler:Handler?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_handler)


        Thread{
            Looper.prepare()
            mHandler = Handler(Handler.Callback {
                Log.e("TAG",it.arg1.toString())
                return@Callback true
            })
            Looper.loop()
        }.start()


        Thread{
            val msg = Message.obtain().apply {
                arg1 = 111
            }
            if (mHandler!=null){
                mHandler!!.sendMessage(msg)
            }
        }.start()
    }
}
