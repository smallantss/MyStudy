package com.example.mystudy.imageloader.policy

import com.example.mystudy.imageloader.request.BitmapRequest

/**
 * 正序
 */
class SerialPolicy:ILoadPolicy {
    override fun compartorto(request1: BitmapRequest, request2: BitmapRequest): Int {
        return request1.serialNo - request2.serialNo
    }
}