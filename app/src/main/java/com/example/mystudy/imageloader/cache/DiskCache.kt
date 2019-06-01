package com.example.mystudy.imageloader.cache

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import com.example.mystudy.imageloader.disk.DiskLruCache
import com.example.mystudy.imageloader.request.BitmapRequest
import java.io.BufferedOutputStream
import java.io.File
import java.io.OutputStream

class DiskCache private constructor(context: Context):BitmapCache {

    private val mCacheDir = "Image"
    private var mDiskLruCache:DiskLruCache?=null

    companion object {
        private var mDiskCache:DiskCache?=null
        private val MB = 1024*1024

        fun getInstance(context: Context):DiskCache{
            if (mDiskCache==null){
                mDiskCache = DiskCache(context)
            }
            return mDiskCache!!
        }
    }

    init {
        initDiskCache(context)
    }

    private fun initDiskCache(context: Context) {
        //得到缓存的目录 Android/data/data/报名/Image
        val directory = getDiskCache(mCacheDir,context)
        if (directory.exists().not()){
            directory.mkdirs()
        }
        mDiskLruCache = DiskLruCache.open(directory,1,1, (50* MB).toLong())

    }

    private fun getDiskCache(mCacheDir: String, context: Context): File {
        context.cacheDir

        return File(Environment.getExternalStorageDirectory(),mCacheDir)
    }


    override fun put(request: BitmapRequest, bitmap: Bitmap) {
        var editor:DiskLruCache.Editor?=null
        var os:OutputStream?=null
        //路径必须是合法字符
        editor = mDiskLruCache!!.edit(request.imageUrl)
        os = editor.newOutputStream(0)
        if (persistBitmap2Disk(bitmap,os)){
            editor.commit()
        }else{
            editor.abort()
        }
    }

    private fun persistBitmap2Disk(bitmap: Bitmap, os: OutputStream?): Boolean {
        val baos = BufferedOutputStream(os)
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos)
        baos.flush()
        baos.close()
        return true
    }

    override fun get(request: BitmapRequest): Bitmap? {
        val snapshot = mDiskLruCache!!.get(request.imageUrl)
        if (snapshot!=null){
            val inputStream = snapshot.getInputStream(0)
            return BitmapFactory.decodeStream(inputStream)
        }
        return null
    }

    override fun remove(request: BitmapRequest) {
        mDiskLruCache!!.remove(request.imageUrl)
    }
}