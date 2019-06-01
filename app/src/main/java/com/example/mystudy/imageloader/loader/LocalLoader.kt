package com.example.mystudy.imageloader.loader

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.example.mystudy.imageloader.request.BitmapRequest
import com.example.mystudy.imageloader.util.BitmapDecoder
import java.io.File

class LocalLoader: AbstractLoader() {
    override fun onLoad(request: BitmapRequest): Bitmap? {
        val path = Uri.parse(request.imageUrl).path
        val file = File(path)
        if (!file.exists()){
            return null
        }else{
            val decoder = object : BitmapDecoder() {
                override fun decodeBitmapWithOptions(options: BitmapFactory.Options): Bitmap {
                    return BitmapFactory.decodeFile(path,options)
                }

            }
            return decoder.decodeBitmap(200,200)
        }

    }
}