package com.example.mystudy.ui

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mystudy.R
import com.example.mystudy.loge

class DialogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog)
        window.decorView.postDelayed({
            Dialog(application).show()
        }, 2000)
    }
}