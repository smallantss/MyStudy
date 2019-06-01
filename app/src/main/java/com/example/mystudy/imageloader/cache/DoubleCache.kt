package com.example.mystudy.imageloader.cache

import android.content.Context
import android.graphics.Bitmap
import com.example.mystudy.imageloader.request.BitmapRequest

class DoubleCache(context: Context):BitmapCache {

    val mMemoryCache = MemoryCache()

    val diskCache = DiskCache.getInstance(context)

    override fun put(request: BitmapRequest, bitmap: Bitmap) {
        mMemoryCache.put(request, bitmap)
        diskCache.put(request, bitmap)
    }

    override fun get(request: BitmapRequest): Bitmap? {
        var bitmap = mMemoryCache.get(request)
        if (bitmap==null){
            bitmap = diskCache.get(request)
            if (bitmap!=null){
                //存入内存
                mMemoryCache.put(request,bitmap)
            }
        }
        return bitmap

    }

    override fun remove(request: BitmapRequest) {
        mMemoryCache.remove(request)
        diskCache.remove(request)
    }
}