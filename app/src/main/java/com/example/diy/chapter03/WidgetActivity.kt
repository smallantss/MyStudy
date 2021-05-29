package com.example.diy.chapter03

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mystudy.R
import com.example.mystudy.ui.loge
import kotlinx.android.synthetic.main.activity_widget.*

class WidgetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_widget)
        ll.post {
            loge("childW:${iv.width},childH:${iv.height}")
            loge("parentW:${ll.width},parentH:${ll.height}")
        }
    }
}