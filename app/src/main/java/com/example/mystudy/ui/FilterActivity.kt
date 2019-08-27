package com.example.mystudy.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.mystudy.R

class FilterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)
    }

    fun onClick(v:View){
        startActivity(Intent().apply {
            action = "com.xwy.aaa"
            addCategory("com.xwy.bbb")
            setDataAndType(Uri.parse("http://abc"),"image/png")
        })
    }
}
