package com.example.mystudy.hook

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import com.example.mystudy.ui.loge

class HookInstrumentation(private val real: Instrumentation) : Instrumentation() {

    fun execStartActivity(context: Context, iBinder: IBinder, token: IBinder?, target: Activity?,
                          intent: Intent?, reqCode: Int, options: Bundle?): ActivityResult? {
        loge("enter:execStartActivity")
        val parameterType = arrayOf(Context::class.java, IBinder::class.java, IBinder::class.java,
                Activity::class.java, Intent::class.java, Int::class.java, Bundle::class.java)
        val data = arrayOf(context, iBinder, token, target, intent, reqCode, options)
        val method = real.javaClass.getDeclaredMethod("execStartActivity", *parameterType)
        val result = method.invoke(real, *data)
        if (result == null) {
            return result
        }
        return result as ActivityResult
    }
}