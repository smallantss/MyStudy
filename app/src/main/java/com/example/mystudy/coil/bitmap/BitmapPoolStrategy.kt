package com.example.mystudy.coil.bitmap

import android.graphics.Bitmap
import android.os.Build.VERSION.SDK_INT
import android.support.annotation.Px
import java.util.*

/**
 * BitmapPool使用的重用算法
 */
internal interface BitmapPoolStrategy {

    companion object {
        operator fun invoke(): BitmapPoolStrategy {
            return when {
                SDK_INT >= 19 -> SizeStrategy()
                else -> AttributeStrategy()
            }
        }
    }

    fun put(bitmap: Bitmap)

    fun get(@Px width: Int, @Px height: Int, config: Bitmap.Config): Bitmap?

    fun removeLast(): Bitmap?

    fun stringify(bitmap: Bitmap): String

    fun stringify(@Px width: Int, @Px height: Int, config: Bitmap.Config): String

}

/**
 * Bitmap的大小大于或等于请求的尺寸的策略
 */
class SizeStrategy : BitmapPoolStrategy {

    //保存大小和bitmap
    private val entries = LinkedMultimap<Int, Bitmap>()

    //每个size分别有多少个
    private val sizes = TreeMap<Int, Int>()

    override fun put(bitmap: Bitmap) {
        //保存bitmap的大小
        val size = bitmap.allocationByteCount
        entries.put(size, bitmap)
        //该bitmap的大小有几个
        val count = sizes[size]
        sizes[size] = if (count == null) 1 else count + 1
    }

    override fun get(width: Int, height: Int, config: Bitmap.Config): Bitmap? {
        val size = Utils.calculateAllocationByteCount(width, height, config)
        //在sizes里面 找到大于或等于size的最小size
        val bestSize = sizes.ceilingKey(size)?.takeIf { it <= MAX_SIZE_MULTIPLE * size } ?: size
        val bitmap = entries.removeLast(bestSize)
        if (bitmap != null) {
            decrementSize(bestSize)
            bitmap.reconfigure(width, height, config)
        }
        return bitmap
    }

    private fun decrementSize(size: Int) {
        val count = sizes.getValue(size)
        if (count == 1) {
            sizes -= size
        } else {
            sizes[size] = count - 1
        }
    }

    override fun removeLast(): Bitmap? {
        val bitmap = entries.removeLast()
        if (bitmap != null) {
            decrementSize(bitmap.allocationByteCount)
        }
        return bitmap
    }

    override fun stringify(bitmap: Bitmap) = "[${bitmap.allocationByteCountCompat}]"

    override fun stringify(width: Int, height: Int, config: Bitmap.Config): String {
        return "[${Utils.calculateAllocationByteCount(width, height, config)}]"
    }

    companion object {
        private const val MAX_SIZE_MULTIPLE = 4
    }

}

/**
 * 根据Bitmap的宽高config来匹配的策略
 */
class AttributeStrategy : BitmapPoolStrategy {

    private val entries = LinkedMultimap<Key, Bitmap>()

    override fun put(bitmap: Bitmap) {
        entries.put(Key(bitmap.width, bitmap.height, bitmap.config), bitmap)
    }

    override fun get(width: Int, height: Int, config: Bitmap.Config): Bitmap? {
        return entries.removeLast(Key(width, height, config))
    }

    override fun removeLast() = entries.removeLast()

    override fun stringify(bitmap: Bitmap) = stringify(bitmap.width, bitmap.height, bitmap.config)

    override fun stringify(width: Int, height: Int, config: Bitmap.Config) = "[$width x $height], $config"

    override fun toString() = "AttributeStrategy: entries=$entries"

    private data class Key(
            @Px val width: Int,
            @Px val height: Int,
            val config: Bitmap.Config)

}