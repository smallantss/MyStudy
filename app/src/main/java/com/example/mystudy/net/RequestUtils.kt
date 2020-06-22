package com.example.mystudy.net

class RequestUtils {


    companion object {

        fun <T> sendRequest(url: String, reqData:String?, responseClazz:Class<T>,listener: IJsonDataListener<T>) {
            val request = JsonHttpRequest()
            val callbackListener = JsonCallbackListener(responseClazz,listener)
            val httpTask = HttpTask(url,reqData,request,callbackListener)
            ThreadPoolManager.getInstance().addTask(httpTask)
        }
    }
}