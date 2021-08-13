package com.example.mystudy.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.mystudy.loge

/**
 * Apk文件更新监听,删除已安装更新文件
 */
class InstallApkBroadCastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        loge("InstallApkBroadCastReceiver:${intent.action}")
        when {
            Intent.ACTION_PACKAGE_REPLACED == intent.action -> {
                loge("升级完成,重新启动")
                exitAndRestart(context)
            }
            Intent.ACTION_PACKAGE_ADDED == intent.action -> {
                loge("安装完成")
            }
            Intent.ACTION_PACKAGE_REMOVED == intent.action -> {
                loge("卸载完成")
            }
        }
    }

    /**
     * 卸载安装重新启动
     */
    private fun exitAndRestart(context: Context) {
        val packageName = context.packageName
        val launchIntentForPackage = context.packageManager.getLaunchIntentForPackage(packageName)
        launchIntentForPackage?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        context.startActivity(launchIntentForPackage)
    }
}