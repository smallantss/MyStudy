package com.example.mystudy.net

interface IHttpRequest {

    fun setUrl(url:String)

    fun setData(data:ByteArray)

    fun setListener(listener: ICallbackListener)

    fun execute()
}