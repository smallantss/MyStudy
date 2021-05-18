package com.example.mystudy.coil.bitmap

import android.app.ActivityManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.StatFs
import androidx.annotation.Px
import androidx.core.content.ContextCompat
import java.io.File

internal object Utils {

    private const val CACHE_DIRECTORY_NAME = "image_cache"

    private const val MIN_DISK_CACHE_SIZE_BYTES = 10L * 1024 * 1024 // 10MB
    private const val MAX_DISK_CACHE_SIZE_BYTES = 250L * 1024 * 1024 // 250MB

    private const val DISK_CACHE_PERCENTAGE = 0.02

    private const val STANDARD_MULTIPLIER = 0.2
    private const val LOW_MEMORY_MULTIPLIER = 0.15

    private const val DEFAULT_MEMORY_CLASS_MEGABYTES = 256

    const val REQUEST_TYPE_ENQUEUE = 0
    const val REQUEST_TYPE_EXECUTE = 1

    /** Prefer hardware bitmaps on API 26 and above since they are optimized for drawing without transformations. */
    val DEFAULT_BITMAP_CONFIG get() = if (Build.VERSION.SDK_INT >= 26) Bitmap.Config.HARDWARE else Bitmap.Config.ARGB_8888

    /** 根据宽高配置返回大小 */
    fun calculateAllocationByteCount(@Px width: Int, @Px height: Int, config: Bitmap.Config?): Int {
        return width * height * config.bytesPerPixel
    }

    fun getDefaultCacheDirectory(context: Context): File {
        return File(context.cacheDir, CACHE_DIRECTORY_NAME).apply { mkdirs() }
    }

    /** Modified from Picasso. */
    fun calculateDiskCacheSize(cacheDirectory: File): Long {
        return try {
            val cacheDir = StatFs(cacheDirectory.absolutePath)
            val size = DISK_CACHE_PERCENTAGE * cacheDir.blockCountCompat * cacheDir.blockSizeCompat
            return size.toLong().coerceIn(MIN_DISK_CACHE_SIZE_BYTES, MAX_DISK_CACHE_SIZE_BYTES)
        } catch (_: Exception) {
            MIN_DISK_CACHE_SIZE_BYTES
        }
    }

    /** Modified from Picasso. */
    fun calculateAvailableMemorySize(context: Context, percentage: Double): Long {
        val memoryClassMegabytes = try {
            val activityManager: ActivityManager = context.requireSystemService()
            val isLargeHeap = (context.applicationInfo.flags and ApplicationInfo.FLAG_LARGE_HEAP) != 0
            if (isLargeHeap) activityManager.largeMemoryClass else activityManager.memoryClass
        } catch (_: Exception) {
            DEFAULT_MEMORY_CLASS_MEGABYTES
        }
        return (percentage * memoryClassMegabytes * 1024 * 1024).toLong()
    }

    fun getDefaultAvailableMemoryPercentage(context: Context): Double {
        return try {
            val activityManager: ActivityManager = context.requireSystemService()
            if (activityManager.isLowRamDeviceCompat) LOW_MEMORY_MULTIPLIER else STANDARD_MULTIPLIER
        } catch (_: Exception) {
            STANDARD_MULTIPLIER
        }
    }

    fun getDefaultBitmapPoolPercentage(): Double {
        return when {
            // Prefer immutable bitmaps (which cannot be pooled) on API 24 and greater.
            Build.VERSION.SDK_INT >= 24 -> 0.0
            // Bitmap pooling is most effective on APIs 19 to 23.
            Build.VERSION.SDK_INT >= 19 -> 0.5
            // The requirements for bitmap reuse are strict below API 19.
            else -> 0.25
        }
    }
}

internal val Bitmap.Config?.bytesPerPixel: Int
    get() = when {
        this == Bitmap.Config.ALPHA_8 -> 1
        this == Bitmap.Config.RGB_565 -> 2
        this == Bitmap.Config.ARGB_4444 -> 2
        Build.VERSION.SDK_INT >= 26 && this == Bitmap.Config.RGBA_F16 -> 8
        else -> 4
    }


internal inline val StatFs.blockCountCompat: Long
    get() = if (Build.VERSION.SDK_INT >= 18) blockCountLong else blockCount.toLong()

@Suppress("DEPRECATION")
internal inline val StatFs.blockSizeCompat: Long
    get() = if (Build.VERSION.SDK_INT >= 18) blockSizeLong else blockSize.toLong()

internal inline val ActivityManager.isLowRamDeviceCompat: Boolean
    get() = Build.VERSION.SDK_INT < 19 || isLowRamDevice

internal inline fun <reified T : Any> Context.requireSystemService(): T {
    return checkNotNull(ContextCompat.getSystemService(this, T::class.java)) { "System service of type ${T::class.java} was not found." }
}

internal inline fun Context.isPermissionGranted(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

internal val Bitmap.allocationByteCountCompat: Int
    get() {
        check(!isRecycled) { "Cannot obtain size for recycled bitmap: $this [$width x $height] + $config" }

        return try {
            if (Build.VERSION.SDK_INT >= 19) {
                allocationByteCount
            } else {
                rowBytes * height
            }
        } catch (_: Exception) {
            Utils.calculateAllocationByteCount(width, height, config)
        }
    }