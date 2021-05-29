package com.example.diy.chapter06

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mystudy.R
import kotlinx.android.synthetic.main.activity_scroller.*

class ScrollerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroller)
        tv.setOnClickListener {
            ll.start()
        }
        tvDel.setOnClickListener {
            ll.reset()
        }
        view2.setOnClickListener {
            Toast.makeText(this, "item2", Toast.LENGTH_SHORT).show()
        }
    }
}