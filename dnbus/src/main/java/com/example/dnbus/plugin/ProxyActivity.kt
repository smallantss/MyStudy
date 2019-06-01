package com.example.dnbus.plugin

import android.content.res.AssetManager
import android.content.res.Resources
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.dnbus.R
import java.lang.RuntimeException

/**
 * 代理Activity管理插件的生命周期
 */
class ProxyActivity : AppCompatActivity() {

    var mPluginApk:PluginApk?=null
    var mPlugin:IPlugin ?=null

    private var className: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        className = intent.getStringExtra("className")
        mPluginApk = PluginManager.getInstance().mPluginApk
        launchActivity()
    }

    private fun launchActivity() {
        mPluginApk?:throw RuntimeException("加载APk啊")
        //实例化
        val clazz = mPluginApk!!.mClassLoader.loadClass(className)
        //此时的Activity不具备生命周期
        val obj = clazz.newInstance()
        if (obj is IPlugin){
            mPlugin = obj
            obj.attach(this)
            val bundle = Bundle().apply {
                putInt("FROM",FROM_OUT)
            }
            obj.onCreate(bundle)
        }
    }

    override fun onStart() {
        mPlugin?.onStart()
        super.onStart()
    }

    override fun getResources(): Resources {
        return if (mPluginApk!=null) {
            mPluginApk!!.mResource
        }else{
            super.getResources()
        }
    }

    override fun getClassLoader(): ClassLoader {
        return if (mPluginApk!=null) {
            mPluginApk!!.mClassLoader
        }else{
            super.getClassLoader()
        }
    }

    override fun getAssets(): AssetManager {
        return if (mPluginApk!=null) {
            mPluginApk!!.mManager
        }else{
            super.getAssets()
        }
    }

}
