package com.example.mystudy.optimize

import android.os.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.mystudy.R
import com.example.mystudy.loge
import kotlinx.android.synthetic.main.activity_block.*
import java.lang.StringBuilder

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

    @RequiresApi(Build.VERSION_CODES.M)
    private fun detectIdleHandler(){
        val queue = Looper.getMainLooper().queue
        val field = queue.javaClass.getDeclaredField("mIdleHandlers")
        field.isAccessible = true
        val newList = MyIdleList()
        field.set(queue,newList)
    }

    class MyIdleList:ArrayList<MessageQueue.IdleHandler>(){
        val map = HashMap<MessageQueue.IdleHandler,MyIdleHandler>()

        override fun add(element: MessageQueue.IdleHandler): Boolean {
            if (element is MessageQueue.IdleHandler){
                val myIdleHandler = MyIdleHandler(element)
                map[element] = myIdleHandler
                return super.add(myIdleHandler);
            }
            return super.add(element)
        }

        override fun remove(element: MessageQueue.IdleHandler): Boolean {
            if (element is MyIdleHandler){
                map.remove(element.idleHandler)
                return super.remove(element.idleHandler)
            }else{
                val myIdleHandler = map.remove(element)
                if (myIdleHandler!=null){
                    return super.remove(myIdleHandler)
                }
            }
            return super.remove(element)
        }
    }

    class MyIdleHandler(val idleHandler: MessageQueue.IdleHandler):MessageQueue.IdleHandler{
        override fun queueIdle(): Boolean {

            return true
        }

    }

}