package com.example.mystudy.imageloader.loader

import android.graphics.Bitmap
import com.example.mystudy.imageloader.request.BitmapRequest

class NullLoader: AbstractLoader() {
    override fun onLoad(request: BitmapRequest): Bitmap? {

        return null
    }
}