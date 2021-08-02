package com.example.mystudy.hook

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import com.example.mystudy.R
import com.example.mystudy.ui.loge
import kotlinx.android.synthetic.main.activity_scroller.*
import kotlinx.coroutines.delay
import java.lang.Exception
import kotlin.concurrent.thread

class HookActivity : AppCompatActivity() {

    private val handler = @SuppressLint("HandlerLeak")
    object :Handler(){
        override fun handleMessage(msg: Message) {
            if (msg.arg1==count){
                loge("正常处理")
                handled = true
            }
        }
    }

    var handled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hook)
        watchMain()
    }

    var count = 0

    private fun watchMain() {
        thread {
            while (true) {
                count++
                handled = false
                val msg = Message.obtain()
                msg.arg1 = count
                handler.sendMessage(msg)
                Thread.sleep(1000)
                if (!handled){
                    loge("出现异常")
                }
            }
        }
    }

    private fun testHooke() {
        HookATM.hook()
        tv.setOnClickListener {
            startActivity(Intent(this, HookActivity::class.java))
        }
        try {
            val field = Activity::class.java.getDeclaredField("mInstrumentation")
            field.isAccessible = true
            val instrumentation = field.get(this) as Instrumentation
            val hook = HookInstrumentation(instrumentation)
            field.set(this, hook)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}