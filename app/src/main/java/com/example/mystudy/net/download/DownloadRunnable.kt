package com.example.mystudy.net.download

import android.os.Environment
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.RandomAccessFile

/**
 * 执行下载的任务
 */
class DownloadRunnable(val index:Int, val url: String, val start: Long, val end: Long) : Runnable {

    override fun run() {
        //重新发起一个可以指定下载部分的请求？
        val okHttpClient = OkHttpClient.Builder().build()
        val request = Request.Builder()
                .url(url)
                .addHeader("Range", "bytes=$start-$end")
                .build()
        val response = okHttpClient.newCall(request).execute()
        val inputStream = response.body()?.byteStream()
        inputStream?.let {
            val file = File(Environment.getExternalStorageDirectory(), "testdownload")
            val randomAccessFile = RandomAccessFile(file, "rwd")
            randomAccessFile.seek(start)
            val buffer = ByteArray(1024 * 10)
            var len = 0
            while (len != -1) {
                len = it.read(buffer)
                if (len!=-1){
                    randomAccessFile.write(buffer,0,len)
                }
            }
            it.close()
            randomAccessFile.close()
            Log.e("TAG","$index is Finished")
            DownloadTask.THREAD_SIZE = 1
        }
    }
}