package com.example.mystudy.net.download

import okhttp3.*
import java.io.IOException

class DownloadDispatcher {

    companion object {
        @Volatile
        private var mDispatcher: DownloadDispatcher? = null

        fun getInstance(): DownloadDispatcher {
            if (mDispatcher == null) {
                synchronized(DownloadDispatcher::class) {
                    if (mDispatcher == null) {
                        mDispatcher = DownloadDispatcher()
                    }
                }
            }
            return mDispatcher!!
        }
    }

    //开始下载，请求下载地址
    fun start(url:String,callback: IDownloadCallback?) {
        val okHttpClient = OkHttpClient.Builder().build()
        val request = Request.Builder()
                .url(url)
                .build()
        val call = okHttpClient.newCall(request)
        call.enqueue(object :Callback{
            override fun onResponse(call: Call, response: Response) {
                //请求完下载地址，才去下载
                val contentLength = response.body()?.contentLength() ?:0
                if (contentLength<=0){
                    //此时获取不到文件大小，直接下载，或者让后台给字段
                    return
                }
                //下载
                val downloadTask = DownloadTask(url,contentLength,callback)
                downloadTask.begin()
            }

            override fun onFailure(call: Call, e: IOException) {
            }
        })
    }
}