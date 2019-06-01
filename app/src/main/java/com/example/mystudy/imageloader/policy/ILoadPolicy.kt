package com.example.mystudy.imageloader.policy

import com.example.mystudy.imageloader.request.BitmapRequest

/**
 * 加载策略
 */
interface ILoadPolicy {

    //两个请求进行优先级比较
    fun compartorto(request1: BitmapRequest,request2: BitmapRequest):Int
}