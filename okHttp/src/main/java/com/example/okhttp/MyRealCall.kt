package com.example.okhttp

class MyRealCall(val client: MyOkhttpClient, request: MyRequest) : MyCall {

    override fun enqueue(callBack: MyCallBack) {
        client.dispatcher?.enqueue(MyAsyncCall(this,callBack))
    }

    override fun execute() {
    }
}