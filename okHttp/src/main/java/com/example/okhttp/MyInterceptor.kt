package com.example.okhttp

interface MyInterceptor {

    //因为最终要把结果传递给下一次
    fun interceptor(chain: MyChain):MyResponse


    interface MyChain{

        //获取请求，进行处理
        fun request():MyRequest

        //将请求处理完，传给下一级
        fun proceed(request: MyRequest):MyResponse

    }

}