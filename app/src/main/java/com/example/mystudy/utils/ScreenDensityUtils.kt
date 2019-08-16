package com.example.mystudy.utils

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks
import android.content.res.Configuration
import android.util.Log

class ScreenDensityUtils private constructor(){

    companion object {

        var appDensity = 0f
        var appScaleDensity = 0f

        //360 * 3 = 1080
        val difineW = 360f


        fun initData(activity: Activity,app:Application){
            val metrics = app.resources.displayMetrics
            if (appDensity==0f){
                //获取到屏幕属性
                appDensity= metrics.density
                appScaleDensity = metrics.scaledDensity

                activity.application.registerComponentCallbacks(object : ComponentCallbacks{
                    override fun onConfigurationChanged(newConfig: Configuration?) {
                        //如果手动修改了字体，则重新设置
                        newConfig?.let {
                            if (it.fontScale>0){
                                appScaleDensity = app.resources.displayMetrics.scaledDensity
                            }
                        }
                    }

                    override fun onLowMemory() {

                    }

                })
            }
            val widthPixels = metrics.widthPixels
            loge("widthPixels-> $widthPixels")//768     780

            //dp  算出比例
            val targetDensity = widthPixels / difineW

            //字体
            val targetScaleDensity = targetDensity * (appScaleDensity / appDensity)


            //屏幕像素密度
            val targetDensityDpi = targetDensity * 160

            //application
            metrics.density = targetDensity
            metrics.scaledDensity = targetScaleDensity
            metrics.densityDpi = targetDensityDpi.toInt()

            //activity
            val displayMetrics = activity.resources.displayMetrics
            displayMetrics.density = targetDensity
            displayMetrics.scaledDensity = targetScaleDensity
            displayMetrics.densityDpi = targetDensityDpi.toInt()


            val heightPixels = metrics.heightPixels
            loge("heightPixels-> $heightPixels")//1184  1200
            loge("appDensity-> $appDensity")//2.0
            loge("scaledDensity-> $appScaleDensity")//2.0
            val densityDpi = metrics.densityDpi
            loge("densityDpi-> $densityDpi")//320
        }

        fun loge(msg:String){
            Log.e("TAG",msg)
        }
    }
}