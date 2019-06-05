package com.example.mystudy.utils

import android.util.Log

class L {

    companion object {

        @JvmStatic
        fun e(any: Any,tag: String = "TAG") {
            Log.e(tag, any.toString())
        }

    }
}