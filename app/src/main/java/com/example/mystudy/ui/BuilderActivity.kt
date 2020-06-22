package com.example.mystudy.ui

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.mystudy.R
import com.example.mystudy.designpattern.builder.DefaultNavigationBar
import com.example.mystudy.net.download.DownloadDispatcher
import com.example.mystudy.okhttp.CacheRequestInterceptor
import com.example.mystudy.okhttp.CacheResponseInterceptor
import kotlinx.android.synthetic.main.activity_builder.*
import okhttp3.*
import java.io.File
import java.io.IOException

class BuilderActivity : AppCompatActivity() {

    private lateinit var okHttpClient: OkHttpClient

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_builder)
        DefaultNavigationBar.Builder(this, root)
                .setLeftText("CCCCCCCCC")
                .hideLeft()
                .create()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), 10)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        initClient()
    }

    private fun initClient() {
        //存放缓存的位置（文件夹）及大小
        val file = File(cacheDir, "http_cache")
        val cache = Cache(file, (10 * 1024 * 1024).toLong())
        okHttpClient = OkHttpClient.Builder()
                .cache(cache)
                //最前 的拦截器
                .addInterceptor(CacheRequestInterceptor(this))
                //最后 的拦截器
                .addNetworkInterceptor(CacheResponseInterceptor())
                .build()
    }

    fun onClick(v: View) {
//        getData()
        download()
    }

    private fun download() {
        DownloadDispatcher.getInstance().start("https://imtt.dd.qq.com/16891/apk/205AC0866FCC77ACE4DD03CB9788CDD2.apk?fsname=com.qq.reader_7.1.1.888_139.apk&csr=1bbd", null)
    }

    private fun getData() {
        val request = Request.Builder()
                .url("https://wanandroid.com/wxarticle/chapters/json")
                .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                Log.e("TAG", "networkResponse->" + response.networkResponse())
                Log.e("TAG", "cacheResponse->" + response.cacheResponse())
                tvResult.text = response.body()?.string()
            }

            override fun onFailure(call: Call, e: IOException) {
                tvResult.text = e?.message
            }
        })
    }
}
