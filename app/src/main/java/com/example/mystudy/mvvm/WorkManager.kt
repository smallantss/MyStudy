package com.example.mystudy.mvvm

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.*
import androidx.work.WorkManager

class WorkManager {

    @RequiresApi(Build.VERSION_CODES.M)
    val contraints = Constraints.Builder()
            //网络连接
            .setRequiredNetworkType(NetworkType.CONNECTED)
            //是否在设备空闲的时候
            .setRequiresDeviceIdle(true)
            //是否在充电的时候
            .setRequiresCharging(true)
            .build()

//    val data = workDataOf("id" to 1)
//
//    val uploadWorkRequest = OneTimeWorkRequestBuilder<UploadWorker>()
//            .setConstraints(contraints)
//            .setInitialDelay(10,TimeUnit.HOURS)
//            .setInputData(data)
//            .addTag("aaa")
//            .build()

//    val period = PeriodicWorkRequestBuilder<UploadWorker>(1000).build()


    fun start(context: Context){
//        WorkManager.getInstance(context).enqueue(uploadWorkRequest)

    }

    fun stop(context: Context){
        WorkManager.getInstance(context).cancelAllWork()
    }

    fun getWorkInfo(context: Context){
        WorkManager.getInstance(context)
    }

    fun workChain(context: Context){
//        WorkManager.getInstance(context).beginWith(uploadWorkRequest).then()
    }

}

class UploadWorker(context: Context,workerParams: WorkerParameters): Worker(context,workerParams) {

    override fun doWork(): Result {


        //获取传入的数据
        val id = inputData.getInt("id",-1)
        uploadImg()
//        val resData = workDataOf("id" to id)
        //返回状态
//        Result.retry()
//        Result.failure()
        return Result.success()

    }

    private fun uploadImg() {

    }


}