package com.example.mystudy.imageloader.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory

abstract class BitmapDecoder {

    fun decodeBitmap(reqW:Int,reqH:Int): Bitmap? {
        //得到边界信息
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        //根据options加载bitmap
        decodeBitmapWithOptions(options)
        //计算图片缩放比
        calSizeWithOption(options,reqW, reqH)

        return decodeBitmapWithOptions(options)
    }

    private fun calSizeWithOption(options: BitmapFactory.Options,reqW: Int,reqH: Int) {
        //原始宽高
        val w = options.outWidth
        val h = options.outHeight
        var inSampleSize = 1
        if (w>reqW || h>reqH){
            //宽高缩放比例
            val hRatio = Math.round((h/reqH).toDouble())
            val wRatio = Math.round((w/reqW).toDouble())

            inSampleSize = Math.max(wRatio,hRatio).toInt()
        }
        options.inSampleSize = inSampleSize
        //每个像素2字节
        options.inPreferredConfig = Bitmap.Config.RGB_565
        options.inJustDecodeBounds = false



    }

    //不传流的引用
    abstract fun decodeBitmapWithOptions(options: BitmapFactory.Options):Bitmap
}