package com.example.mystudy.imageloader.cache

import android.graphics.Bitmap
import com.example.mystudy.imageloader.request.BitmapRequest

interface BitmapCache {

    //缓存bitmap
    fun put(request: BitmapRequest,bitmap: Bitmap)

    //获取
    fun get(request: BitmapRequest):Bitmap?

    //移除缓存对象
    fun remove(request: BitmapRequest)
}