package com.example.mystudy.net

import android.util.Log
import java.util.concurrent.*

class ThreadPoolManager {

    companion object {
        val CORE_COUNT = 3
        val MAX_COUNT = 10
        private val threadPoolManager = ThreadPoolManager()

        fun getInstance(): ThreadPoolManager {
            return threadPoolManager
        }
    }

    //任务队列，保存网络请求任务，先进先出
    private val mQueue = LinkedBlockingQueue<Runnable>()

    //线程池，处理任务
    lateinit var mThreadPoolExecutor: ThreadPoolExecutor

    //创建一个工作线程，主要从任务队列中取线程
    private val coreThread = object : Runnable {
        var runn: Runnable? = null
        override fun run() {
            while (true) {
                //从任务队列中获取任务
                runn = mQueue.take()
                //交给线程池处理
                mThreadPoolExecutor.execute(runn)
            }
        }
    }

    //创建延迟队列，添加失败的
    private val delayQueue = DelayQueue<HttpTask<*>>()

    fun <T> addDelayTask(httpTask: HttpTask<T>) {
        httpTask.delayTime = 3000
        delayQueue.offer(httpTask)
    }

    //创建延迟线程
    private val delayThread = object : Runnable {
        var httpTask: HttpTask<*>? = null
        override fun run() {
            while (true) {
                //如果当前任务重试次数小于3次，继续将其丢到线程池处理，否则直接放弃
                httpTask = delayQueue.take()
                httpTask?.let {
                    if (it.retryCount < 3) {
                        mThreadPoolExecutor.execute(httpTask)
                        it.retryCount++
                    }else{
                        Log.e("重试机制","超过3次")
                    }
                }
            }
        }
    }


    init {
        mThreadPoolExecutor = ThreadPoolExecutor(CORE_COUNT, MAX_COUNT, 10, TimeUnit.SECONDS,
                ArrayBlockingQueue<Runnable>(4),
                RejectedExecutionHandler { r, executor ->
                    //拒绝的线程处理，重新放入队列
                    addTask(r)
                })
        mThreadPoolExecutor.execute(coreThread)
        mThreadPoolExecutor.execute(delayThread)
    }

    //添加任务到队列
    fun addTask(runnable: Runnable) {
        mQueue.add(runnable)
    }


}