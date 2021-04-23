package com.example.mystudy.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.mystudy.R
import kotlin.random.Random

class AopActivity : AppCompatActivity() {

    val TAG = "AopActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aop)

    }

    fun onClick(view:View){
        startActivity(Intent(this, FragmentActivity::class.java))
//        shakeFun()
//        talk()
    }

    fun shakeFun(){
        val startTimeMillis = System.currentTimeMillis()
        Thread.sleep(Random.nextInt(2000).toLong())
        val endTimeMillis = System.currentTimeMillis()
        val duration = endTimeMillis - startTimeMillis
        Log.e(TAG,"摇一摇花费了$duration")
    }

    fun talk(){
        val startTimeMillis = System.currentTimeMillis()
        Thread.sleep(Random.nextInt(2000).toLong())
        val endTimeMillis = System.currentTimeMillis()
        val duration = endTimeMillis - startTimeMillis
        Log.e(TAG,"聊天花费了$duration")
    }




}
