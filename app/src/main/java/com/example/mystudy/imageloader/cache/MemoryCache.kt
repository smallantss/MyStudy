package com.example.mystudy.imageloader.cache

import android.graphics.Bitmap
import android.util.LruCache
import com.example.mystudy.imageloader.request.BitmapRequest

class MemoryCache:BitmapCache {

    //缓存淘汰算法：只保留最近使用的
    private var mLrcCache:LruCache<String,Bitmap>

    init {
        //取1/8的空闲内存
        mLrcCache = object :LruCache<String,Bitmap>((Runtime.getRuntime().freeMemory()/1024/8).toInt()){
            override fun sizeOf(key: String?, value: Bitmap?): Int {
                return value!!.rowBytes* value.height
            }
        }

    }

    override fun put(request: BitmapRequest, bitmap: Bitmap) {
        mLrcCache.put(request.imageUrl,bitmap)
    }

    override fun get(request: BitmapRequest): Bitmap? {
        return mLrcCache.get(request.imageUrl)
    }

    override fun remove(request: BitmapRequest) {
        mLrcCache.remove(request.imageUrl)
    }
}