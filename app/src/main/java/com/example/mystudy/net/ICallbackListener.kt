package com.example.mystudy.net

import java.io.InputStream

interface ICallbackListener {

    fun onSuccess(inputStream:InputStream)

    fun onFailure()
}