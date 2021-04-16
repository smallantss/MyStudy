package com.example.mystudy.coil.bitmap

import android.content.ComponentCallbacks2
import android.graphics.Bitmap
import android.graphics.Bitmap.createBitmap
import android.graphics.Color
import android.os.Build

internal class RealBitmapPool(
        private val maxSize: Int,
        private val allowedConfigs: Set<Bitmap.Config>,
        private val strategy: BitmapPoolStrategy) : BitmapPool {

    private val bitmaps = hashSetOf<Bitmap>()
    private var currentSize = 0
    private var hits = 0
    private var misses = 0
    private var puts = 0
    private var evictions = 0

    override fun put(bitmap: Bitmap) {
        if (bitmap.isRecycled) {
            return
        }
        val size = bitmap.allocationByteCountCompat
        if (bitmap.isMutable || size > maxSize || bitmap.config !in allowedConfigs) {
            bitmap.recycle()
            return
        }
        if (bitmap in bitmaps) {
            return
        }
        strategy.put(bitmap)
        bitmaps += bitmap
        currentSize += size
        puts++
        trimToSize(maxSize)
    }

    @Synchronized
    private fun trimToSize(size: Int) {
        while (currentSize > size) {
            val removed = strategy.removeLast()
            if (removed == null) {
                currentSize = 0
                return
            }
            bitmaps -= removed
            currentSize -= removed.allocationByteCountCompat
            evictions++
            removed.recycle()
        }
    }

    override fun get(width: Int, height: Int, config: Bitmap.Config): Bitmap {
        return getOrNull(width, height, config) ?: createBitmap(width, height, config)
    }

    override fun getOrNull(width: Int, height: Int, config: Bitmap.Config): Bitmap? {
        return getDirtyOrNull(width,height, config)?.apply { eraseColor(Color.TRANSPARENT) }
    }

    override fun getDirty(width: Int, height: Int, config: Bitmap.Config): Bitmap {
        return getDirtyOrNull(width, height, config)?: createBitmap(width, height, config)
    }

    override fun getDirtyOrNull(width: Int, height: Int, config: Bitmap.Config): Bitmap? {
        val result = strategy.get(width, height, config)
        if (result==null){
            misses++
        }else{
            bitmaps-=result
            currentSize-=result.allocationByteCountCompat
            hits++
            normalize(result)
        }
        return result
    }

    private fun normalize(bitmap: Bitmap) {
        bitmap.density = Bitmap.DENSITY_NONE
        bitmap.setHasAlpha(true)
        if (Build.VERSION.SDK_INT >= 19) bitmap.isPremultiplied = true
    }

    override fun trimMemory(level: Int) {
        if (level >= ComponentCallbacks2.TRIM_MEMORY_BACKGROUND) {
            clearMemory()
        } else if (level in ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW until ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            trimToSize(currentSize / 2)
        }
    }

    override fun clear() {
        clearMemory()
    }

    fun clearMemory() {
        trimToSize(-1)
    }

    companion object {
        @OptIn(ExperimentalStdlibApi::class)
        private val ALLOWED_CONFIGS = buildSet {
            add(Bitmap.Config.ALPHA_8)
            add(Bitmap.Config.RGB_565)
            add(Bitmap.Config.ARGB_4444)
            add(Bitmap.Config.ARGB_8888)
            if (Build.VERSION.SDK_INT >= 26) add(Bitmap.Config.RGBA_F16)
        }
    }


}