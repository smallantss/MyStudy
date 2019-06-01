package com.example.dnbus.plugin

import android.app.Activity
import android.os.Bundle

/**
 * 插件Activity生命周期的管理
 */
const val FROM_OUT = 0x1111
const val FROM_INT = 0x1112
interface IPlugin {

    fun attach(proxy: Activity)

    fun onCreate(savedInstanceState:Bundle)

    fun onStart()

    fun onResume()

    fun onPause()

    fun onStop()

    fun onRestart()

    fun onDestroy()
}