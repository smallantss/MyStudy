package com.example.mystudy.net.download

class DownloadManager {

    private val mManager:DownloadManager by lazy {
        DownloadManager()
    }

    init {

    }

    fun startDownload(url:String,callback:IDownloadCallback){

//        val task = DownloadTask(url)
//        DownloadDispatcher.getInstance().start()
    }
}