package com.example.mystudy.imageloader.loader

import android.graphics.Bitmap
import com.example.mystudy.imageloader.request.BitmapRequest

interface Loader {

    fun loadImage(request: BitmapRequest)
}