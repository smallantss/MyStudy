package com.example.okhttp

class MyAsyncCall(val myRealCall: MyRealCall,val callBack: MyCallBack) :Runnable {

    override fun run() {
        //负责网络请求相关
        callBack.onResponse(myRealCall,MyResponse())
    }
}