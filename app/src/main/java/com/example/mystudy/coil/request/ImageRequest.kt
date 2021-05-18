package com.example.mystudy.coil.request

import android.content.Context
import android.widget.ImageView
import androidx.annotation.MainThread
import com.example.mystudy.coil.target.ImageViewTarget
import com.example.mystudy.coil.target.Target

class ImageRequest private constructor(
        val context: Context,
        val data: Any?,
        val target: Target?,
        val listener: Listener?) {

    interface Listener {
        @MainThread
        fun onStart(request: ImageRequest) {
        }

        @MainThread
        fun onCancel(request: ImageRequest) {
        }

        @MainThread
        fun onError(request: ImageRequest, throwable: Throwable) {
        }

        @MainThread
        fun onSuccess(request: ImageRequest) {
        }
    }

    class Builder {
        private val context: Context
        private var data: Any?
        private var target: Target?

        constructor(context: Context) {
            this.context = context
            data = null
            target = null
        }

        fun data(data: Any?) = apply {
            this.data = data
        }

        fun target(imageView: ImageView) = target(ImageViewTarget(imageView))

        fun target(target: Target?) = apply {
            this.target = target
        }

        fun build(): ImageRequest {
            return ImageRequest(context = context, data = data, target = target, listener = null)
        }
    }
}

