package com.example.mystudy.coil.bitmap

import android.graphics.Bitmap
import androidx.annotation.Px

interface BitmapPool {

    fun put(bitmap: Bitmap)

    /**
     * 返回给定的Bitmap，仅包含透明像素
     * 会清空Bitmap中所有像素，因此比getDirty慢
     */
    fun get(@Px width: Int, @Px height: Int, config: Bitmap.Config): Bitmap

    fun getOrNull(@Px width: Int, @Px height: Int, config: Bitmap.Config): Bitmap?

    /**
     * 返回Bitmap像素可能包含其他数据
     */
    fun getDirty(@Px width: Int, @Px height: Int, config: Bitmap.Config): Bitmap

    fun getDirtyOrNull(@Px width: Int, @Px height: Int, config: Bitmap.Config): Bitmap?

    fun trimMemory(level:Int)

    /**
     * 清空所有的Bitmap并释放内存
     */
    fun clear()
}