package com.example.mystudy.imageloader.request

import android.util.Log
import com.example.mystudy.imageloader.loader.LoadManager
import java.util.concurrent.BlockingQueue

/**
 * 转发器
 * 请求转发线程 不断从请求队列中获取请求
 */
class RequestDispatcher(var mRequestQueue:BlockingQueue<BitmapRequest>): Thread() {


    override fun run() {
        while (!isInterrupted){
            //获取请求队列的请求对象 阻塞式函数
            val request = mRequestQueue.take()

            val schema = parseSchema(request.imageUrl)
            val loader = LoadManager.getInstance().getLoader(schema)
            loader?.loadImage(request)

        }
    }

    //判断本地还是网络
    private fun parseSchema(imageUrl: String):String {
        if (imageUrl.startsWith("://")){
            return imageUrl.split("://")[0]
        }else{
            Log.e("TAG","不支持此类型")
            return ""
        }
    }
}