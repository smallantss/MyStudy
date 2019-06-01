package com.example.mystudy.imageloader.request

import android.util.Log
import java.util.concurrent.BlockingDeque
import java.util.concurrent.BlockingQueue
import java.util.concurrent.PriorityBlockingQueue
import java.util.concurrent.atomic.AtomicInteger

/**
 * 请求队列
 */
class RequestQueue(val threadCount:Int){

    /**
     * 阻塞式队列
     * 多线程共享
     * 生产效率 远大于消费效率
     * displayImage()
     * 使用优先级队列
     * 优先级高的队列先被消费
     * 每个产品都有编号
     */
    private val mRequestQueue:BlockingQueue<BitmapRequest> = PriorityBlockingQueue<BitmapRequest>()


    //一组转发器
    private var mDispatcher: ArrayList<RequestDispatcher>? = null

    //线程安全的编号
    private var no = AtomicInteger(0)

    fun addRequest(request:BitmapRequest){
        //判断请求队列是否包含该请求
        if (!mRequestQueue.contains(request)){
            //请求进行编号 保证线程安全
            request.serialNo = no.incrementAndGet()
            mRequestQueue.add(request)
        }else{
            Log.e("TAG", request.serialNo.toString())
        }
    }

    //开始请求
    fun start(){
        //先停止之前的
        stop()
        startDispatcher()
    }

    //开启转发器
    private fun startDispatcher() {
        mDispatcher = ArrayList(threadCount)
        for(i in 0 until threadCount){
            val p = RequestDispatcher(mRequestQueue)
            mDispatcher!![i] = p
            mDispatcher!![i].start()
        }
    }

    //停止请求
    fun stop(){

    }
}