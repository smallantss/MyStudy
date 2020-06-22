package com.example.mystudy.utils

import android.util.Log

class LogUtils {

    companion object {

        @JvmStatic
        fun e(any: Any, tag: String = "TAG") {
            Log.e(tag, any.toString())
        }

        @JvmStatic
        fun d(any: Any, tag: String = "TAG") {
            Log.d(tag, any.toString())
        }

        @JvmStatic
        fun w(any: Any, tag: String = "TAG") {
            Log.w(tag, any.toString())
        }

        @JvmStatic
        fun i(any: Any, tag: String = "TAG") {
            Log.i(tag, any.toString())
        }

    }
}