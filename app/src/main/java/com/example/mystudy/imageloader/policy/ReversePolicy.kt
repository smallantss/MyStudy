package com.example.mystudy.imageloader.policy

import com.example.mystudy.imageloader.request.BitmapRequest

/**
 * 反序
 * 先进先出
 */
class ReversePolicy:ILoadPolicy {
    override fun compartorto(request1: BitmapRequest, request2: BitmapRequest):Int {
        return request2.serialNo - request1.serialNo
    }
}