package com.example.mystudy.ui

import android.os.*
import android.support.v7.app.AppCompatActivity
import com.example.mystudy.R
import kotlinx.android.synthetic.main.activity_block.*
import java.lang.StringBuilder
import kotlin.math.min

class BlockActivity : AppCompatActivity() {

    //判断方法执行前还是执行后调用println
    private var printStart = true

    private var printStartTime = 0L

    //记录执行超过1s的方法
    private val minTime = 1000L
    val sb = StringBuilder()
    private val runnable = Runnable {
        //获取方法栈
        sb.clear()
        for (stackTrackElement in Looper.getMainLooper().thread.stackTrace) {
            sb.append(stackTrackElement.toString()).append("\r\n")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_block)

        btn.setOnClickListener {
            SystemClock.sleep(500)
        }
        btn2.setOnClickListener {
            SystemClock.sleep(1000)
        }
        btn3.setOnClickListener {
            SystemClock.sleep(800)
        }
        btn7.setOnClickListener {
            SystemClock.sleep(700)
        }

        val handlerThread = HandlerThread("DelayThread")
        handlerThread.start()
        //子线程的handler，为了在子线程记录信息
        val delayHandler = Handler(handlerThread.looper)

        //在主线程执行方法的时候，都会调用这里的方法
        Looper.getMainLooper().setMessageLogging {
            if (printStart) {
                printStart = false
                printStartTime = System.currentTimeMillis()
                //为了记录调用的方法，需要在方法执行前记录方法栈信息。
                delayHandler.removeCallbacks(runnable)
                //延迟发送，记录阻塞方法栈信息
                delayHandler.postDelayed(runnable, (minTime * 0.8).toLong())
            } else {
                printStart = true
                //未超过时间，证明正常执行完，此时已经打印了。
                delayHandler.removeCallbacks(runnable)
                (System.currentTimeMillis() - printStartTime).let {
                    if (it > minTime) {
                        loge("方法执行的总时长:$it")
                        //超过了时间，证明阻塞了，打印记录的信息
                        loge(sb.toString())
                    }
                }
            }
        }
    }


}