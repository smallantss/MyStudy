package com.example.mystudy.net.download

import android.util.Log
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class DownloadTask(val url: String, val length: Long, val callback: IDownloadCallback?) {

    companion object {
        private val CPU_COUNT = Runtime.getRuntime().availableProcessors()
//        private val THREAD_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4))
        var THREAD_SIZE = 3
    }

    private val mExecutor = ThreadPoolExecutor(0, THREAD_SIZE, 30, TimeUnit.SECONDS, SynchronousQueue<Runnable>(), ThreadFactory {
        Thread(it, "DownloadThread").apply {
            isDaemon = false
        }
    })

    fun begin() {
        Log.e("TAG", "download is begin,sun is ${THREAD_SIZE - 1}")
        val part = length / THREAD_SIZE
        for (i in 0 until THREAD_SIZE) {
            //根据线程数计算每个线程需要下载多少
            val start = i * part
            val end = if (i == THREAD_SIZE - 1) {
                length
            } else {
                start + part
            }
            //并把各个线程需要下载的任务通过runnable去执行
            val downloadRunnable = DownloadRunnable(i, url, start, end)
            mExecutor.execute(downloadRunnable)
        }
    }
}