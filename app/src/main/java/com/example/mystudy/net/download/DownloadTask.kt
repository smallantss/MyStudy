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

    @Volatile
    private var mSuccessNum = 0
    private val runnableList = ArrayList<DownloadRunnable>()

    private val mExecutor = ThreadPoolExecutor(0, THREAD_SIZE, 30, TimeUnit.SECONDS, SynchronousQueue<Runnable>(), ThreadFactory {
        Thread(it, "DownloadThread").apply {
            isDaemon = false
        }
    })

    //这里如果是支持断点的话，则需要保存各个线程下载的进度，下次下载直接从进度开始下
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

            /**
             * TODO 这里需要查询数据库，获取每个线程对应的progress
             */
            val downloadBeans = queryDownloadBeans()
            var downloadBean = getMatchDownloadBean(downloadBeans,i)
            if (downloadBean==null){
                downloadBean = DownloadBean(i,0,end-start)
            }

            //并把各个线程需要下载的任务通过runnable去执行
            val downloadRunnable = DownloadRunnable(downloadBean,i, url, start, end,object :IDownloadCallback{
                override fun onFailure() {
                }

                override fun onSuccess() {
                    mSuccessNum++
                    if (mSuccessNum==THREAD_SIZE){
                        //才代表真正成功
                    }
                }
            })
            runnableList.add(downloadRunnable)
            mExecutor.execute(downloadRunnable)
        }
    }

    //获取线程id对应的线程
    private fun getMatchDownloadBean(downloadBeans: List<DownloadBean>, i: Int): DownloadBean? {
        return downloadBeans.first {
            it.threadId == i
        }
    }

    private fun queryDownloadBeans():List<DownloadBean>{
        return ArrayList()
    }

    fun stop(){
        runnableList.forEach {
            it.stop()
        }
    }
}