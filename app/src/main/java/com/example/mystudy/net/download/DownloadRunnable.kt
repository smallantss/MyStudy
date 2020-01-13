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
class DownloadRunnable(val bean:DownloadBean,val index:Int, val url: String, var start: Long, val end: Long,val callback: IDownloadCallback) : Runnable {

    private var stop = false

    override fun run() {
        try {
            //需要加上上次的进度
            start+=bean.progress
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
                    if (stop){
                        break
                    }
                    len = it.read(buffer)
                    if (len!=-1){
                        randomAccessFile.write(buffer,0,len)
                    }
                    bean.progress+=len
                }
                it.close()
                randomAccessFile.close()
                Log.e("TAG","$index is Finished")
                DownloadTask.THREAD_SIZE = 1
                saveBean(bean)
            }
        }catch (e:Exception){
            e.printStackTrace()
            callback.onFailure()
        }finally {
            callback.onSuccess()
        }

    }

    //TODO 保存到数据库,如果全部下完了，就不需要保存了
    private fun saveBean(bean: DownloadBean) {
        if (bean.progress==bean.total){
            return
        }
    }

    fun stop(){
        stop = true
    }
}