package com.example.mystudy

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import com.example.mystudy.service.InstallApkBroadCastReceiver
import com.example.mystudy.utils.FileProvider7
import com.example.opengl.opencv.OpencvActivity
import java.io.DataOutputStream
import java.io.File
import java.io.IOException
import java.io.OutputStream

fun loge(msg: String, tag: String = "TAG") {
    Log.e(tag, msg)
}

fun Context.silenceInstall(apkPath: String): Boolean {
    loge("apk:$apkPath")
    val managerClass: Class<*> = packageManager.javaClass
    try {
        if (Build.VERSION.SDK_INT >= 21) {
            val pio = Class.forName("android.app.PackageInstallObserver")
            val constructor = pio.getDeclaredConstructor()
            constructor.isAccessible = true
            val installObserver = constructor.newInstance()
            val method = managerClass.getDeclaredMethod(
                    "installPackage",
                    Uri::class.java, pio,
                    Int::class.javaPrimitiveType,
                    String::class.java
            )
            method.isAccessible = true
            method.invoke(
                    packageManager, Uri.fromFile(File(apkPath)),
                    installObserver,
                    2,
                    null
            )
        } else {
            val method = managerClass.getDeclaredMethod(
                    "installPackage",
                    Uri::class.java,
                    Class.forName("android.content.pm.IPackageInstallObserver"),
                    Int::class.javaPrimitiveType,
                    String::class.java
            )
            method.isAccessible = true
            method.invoke(
                    packageManager,
                    FileProvider7.getUriForFile(this, File(apkPath)),
                    null,
                    2,
                    null
            )
        }
        loge("安装成功")
//        start(this)
//        execLinuxCommand()
        return true
    } catch (e: Exception) {
        e.printStackTrace()
        loge("安装出错:${e.message}")
    }
    return false
}

private fun start(context: Context) {
//    val packageName = context.packageName
//    val launchIntentForPackage = context.packageManager.getLaunchIntentForPackage(packageName)
//    launchIntentForPackage?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//    context.startActivity(launchIntentForPackage)
    val intent = Intent(context, OpencvActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    //重启应用，得使用PendingIntent
    val restartIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
    val mAlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    mAlarmManager[AlarmManager.RTC, System.currentTimeMillis() + 5000] = restartIntent // 2秒钟后重启应用
}

private fun execLinuxCommand() {
    val cmd = "sleep 120; am start -n com.example.mystudy/com.example.opengl.opencv.OpencvActivity"
    //Runtime对象
    val runtime = Runtime.getRuntime()
    try {
        val localProcess = runtime.exec("su")
        val localOutputStream: OutputStream = localProcess.outputStream
        val localDataOutputStream = DataOutputStream(localOutputStream)
        localDataOutputStream.writeBytes(cmd)
        localDataOutputStream.flush()
        loge("设备准备重启")
    } catch (e: IOException) {
        e.printStackTrace()
    }
}