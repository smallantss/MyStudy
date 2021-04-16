package com.example.mystudy.imageloader.loader

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.mystudy.imageloader.request.BitmapRequest
import com.example.mystudy.imageloader.util.BitmapDecoder
import java.io.BufferedInputStream
import java.io.InputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class UrlLoader : AbstractLoader() {

    private lateinit var bitmapDecoder: BitmapDecoder

    override fun onLoad(request: BitmapRequest): Bitmap? {
        try {
            var httpUrlConnection: HttpURLConnection? = null
            var inputStream: BufferedInputStream? = null
            httpUrlConnection = URL(request.imageUrl).openConnection() as HttpURLConnection?
            inputStream = BufferedInputStream(httpUrlConnection?.inputStream)
            inputStream.mark(inputStream.available())
            val finalInputStream = inputStream
            bitmapDecoder = object : BitmapDecoder() {
                override fun decodeBitmapWithOptions(options: BitmapFactory.Options): Bitmap {
                    val bitmap = BitmapFactory.decodeStream(finalInputStream, null, options)
                    //第一次读流
                    if (options.inJustDecodeBounds) {
                        //第一次读完后为下一次读流重置
                        finalInputStream.reset()
                    } else {
                        //第二次关闭流
                        finalInputStream.close()
                    }
                    return bitmap!!
                }

            }
            return bitmapDecoder.decodeBitmap(200, 200)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }
}