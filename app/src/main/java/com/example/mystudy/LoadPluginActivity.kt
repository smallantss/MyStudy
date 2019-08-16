package com.example.mystudy

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.dnbus.plugin.PluginManager
import com.example.dnbus.plugin.ProxyActivity

/**
 * 加载插件的Activity
 */
class LoadPluginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_plugin)
        PluginManager.getInstance().initContext(this)
    }

    fun loadApk(view:View){
        //apk路径
        val path = cacheDir.absolutePath+"plugin.apk"
        PluginManager.getInstance().loadApk(path)
    }

    fun jump(view:View){
        startActivity(Intent(this,ProxyActivity::class.java).apply {
            //插件的Activity的类名
            putExtra("className","com.xwy.plugin.ThirdActivity")
        })
    }
}
