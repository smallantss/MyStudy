package com.example.mystudy.net

import android.os.Handler
import android.os.Looper
import com.alibaba.fastjson.JSON
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class JsonCallbackListener<T>(val responseClazz: Class<T>,val jsDataListener:IJsonDataListener<T>) : ICallbackListener {

    private val handler = Handler(Looper.getMainLooper())

    override fun onSuccess(inputStream: InputStream) {
        val response = getContent(inputStream)
        val clazz = JSON.parseObject(response, responseClazz)
        handler.post {
            jsDataListener.onSuccess(clazz)
        }
    }

    private fun getContent(inputStream: InputStream): String {
        val reader = BufferedReader(InputStreamReader(inputStream))
        val sb = StringBuilder()
        var line: String?
        while (true) {
            line = reader.readLine()
            if (line != null) {
                sb.append(line + "\n")
            } else {
                break
            }
        }
        inputStream.close()
        reader.close()
        return sb.toString()

    }

    override fun onFailure() {

    }
}