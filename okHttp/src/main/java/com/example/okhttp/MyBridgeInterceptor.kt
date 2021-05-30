package com.example.okhttp

class MyBridgeInterceptor : MyInterceptor {

    override fun interceptor(chain: MyInterceptor.MyChain): MyResponse {
        //做一些操作
        val request = chain.request()
        request.headers!![""] = ""
        return chain.proceed(request)

    }
}