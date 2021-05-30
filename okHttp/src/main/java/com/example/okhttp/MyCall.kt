package com.example.okhttp

interface MyCall {

    fun enqueue(callBack: MyCallBack)

    fun execute()
}