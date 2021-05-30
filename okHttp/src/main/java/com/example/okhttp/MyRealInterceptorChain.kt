package com.example.okhttp

import kotlin.properties.Delegates

class MyRealInterceptorChain : MyInterceptor.MyChain {

    private var mInterceptors: List<MyInterceptor> by Delegates.notNull()
    private var mIndex: Int by Delegates.notNull()
    private var mRequest: MyRequest by Delegates.notNull()

    constructor(interceptors: List<MyInterceptor>, index: Int, request: MyRequest) {
        mInterceptors = interceptors
        mIndex = index
        mRequest = request
    }

    override fun request() = mRequest

    override fun proceed(request: MyRequest): MyResponse {
        //把集合传进来
        val nextChain = MyRealInterceptorChain(mInterceptors, mIndex + 1, mRequest)
        //走到这里，进入下一个拦截器
        val interceptor = mInterceptors[mIndex]
        //执行下一个拦截器的interceptor方法
        return interceptor.interceptor(nextChain)
    }
}