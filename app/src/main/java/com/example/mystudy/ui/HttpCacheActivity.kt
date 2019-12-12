package com.example.mystudy.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.mystudy.R
import com.example.mystudy.okhttp.CacheRequestInterceptor
import com.example.mystudy.okhttp.CacheResponseInterceptor
import kotlinx.android.synthetic.main.activity_builder.*
import okhttp3.*
import java.io.File
import java.io.IOException

class HttpCacheActivity : AppCompatActivity() {

    private lateinit var okHttpClient: OkHttpClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_http_cache)

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
        val request = Request.Builder()
                .url("https://wanandroid.com/wxarticle/chapters/json")
                .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                Log.e("TAG","networkResponse->"+response.networkResponse())
                Log.e("TAG","cacheResponse->"+response.cacheResponse())
                tvResult.text = response.body()?.string()
            }

            override fun onFailure(call: Call, e: IOException) {
                tvResult.text = e?.message
            }
        })
    }
}
