package com.example.mystudy.imageloader.loader

import android.graphics.Bitmap
import android.widget.ImageView
import com.example.mystudy.imageloader.config.DisplayConfig
import com.example.mystudy.imageloader.config.ImageLoaderConfig
import com.example.mystudy.imageloader.request.BitmapRequest
import com.example.mystudy.imageloader.request.RequestQueue
import java.lang.UnsupportedOperationException

/**
 * 简易的图片加载器
 */
class SimpleImageLoader private constructor(){

    //配置
    var config:ImageLoaderConfig?=null
    //请求队列
    var mRequestQueue:RequestQueue?=null


    private constructor(imageLoaderConfig: ImageLoaderConfig) : this() {
        config = imageLoaderConfig
        //初始化请求队列
        mRequestQueue = RequestQueue(imageLoaderConfig.threadCount)
        //开启请求队列
        mRequestQueue?.start()
    }

    companion object {

        private var mInstance:SimpleImageLoader?=null

        fun getInstance(config: ImageLoaderConfig):SimpleImageLoader{
            if (mInstance==null){
                synchronized(SimpleImageLoader::class.java){
                    if (mInstance==null){
                        mInstance = SimpleImageLoader(config)
                    }
                }
            }
            return mInstance!!
        }

        fun getInstance():SimpleImageLoader{
            if (mInstance==null){
                throw UnsupportedOperationException("没有初始化")
            }
            return mInstance!!
        }

        interface ImageListener{

            fun onComplete(imageView: ImageView,bitmap:Bitmap,uri: String)
        }
    }

    fun displayImage(imageView:ImageView,uri:String){
        displayImage(imageView, uri,null,null)
    }

    fun displayImage(imageView:ImageView,uri:String,config:DisplayConfig?,listener:ImageListener?){
        //实例化一个请求
        val bitmapRequest = BitmapRequest(imageView,uri,config,listener)
        //添加到队列
        mRequestQueue?.addRequest(bitmapRequest)
    }
}