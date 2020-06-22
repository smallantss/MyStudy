package com.example.mystudy.net

import java.io.BufferedOutputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

//真正执行的请求类
class JsonHttpRequest:IHttpRequest {

    private lateinit var url:String
    private lateinit var data: ByteArray
    private lateinit var listener: ICallbackListener
    override fun setUrl(url: String) {
        this.url = url
    }

    override fun setData(data: ByteArray) {
        this.data = data
    }

    override fun setListener(listener: ICallbackListener) {
        this.listener = listener
    }

    override fun execute() {
        val url = URL(url)
        //打开链接
        val urlConnection = url.openConnection() as HttpsURLConnection
        urlConnection.apply {
            //连接超时时长
            connectTimeout = 10000
            //是否使用缓存
            useCaches = false
            //是否可以重定向
            instanceFollowRedirects = false
            //响应超时时长
            readTimeout = 10000
            //是否可写读数据
            doInput = true
            doOutput = true
            //请求方式
            requestMethod = "POST"
            //消息类型
            setRequestProperty("Content-Type","application/json;charset=UTF-8")
            val os = outputStream
            BufferedOutputStream(os).apply {
                //数据写入缓冲区，刷新
                write(data)
                flush()
                close()
            }
            os.close()
            if (responseCode==HttpURLConnection.HTTP_OK){
                val i = inputStream
                listener.onSuccess(i)
            }
            disconnect()
        }

    }
}