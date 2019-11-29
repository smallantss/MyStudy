package com.example.mystudy.net

import com.alibaba.fastjson.JSON
import java.nio.charset.Charset
import java.util.concurrent.Delayed
import java.util.concurrent.TimeUnit

//对网络请求所需要的数据进行封装
class HttpTask<T>(url: String, requestData: T, val httpRequest: IHttpRequest, listener: ICallbackListener) : Runnable, Delayed {

    //设置delayTime要和当前时间相加
    var delayTime: Long = 0
        set(value) {
            field = System.currentTimeMillis() + value
        }
    var retryCount: Int = 0

    override fun compareTo(other: Delayed?): Int {
        return 0
    }

    override fun getDelay(unit: TimeUnit?): Long {
        return unit?.convert(delayTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS) ?: 0
    }

    init {
        httpRequest.setUrl(url)
        httpRequest.setListener(listener)
        val content = JSON.toJSONString(requestData)
        httpRequest.setData(content.toByteArray(Charset.defaultCharset()))
    }

    override fun run() {
        try {
            //最终的执行实际上是httpRequest来执行
            httpRequest.execute()
        }catch (e:Exception){
                 ThreadPoolManager.getInstance().addDelayTask(this)
        }
    }
}