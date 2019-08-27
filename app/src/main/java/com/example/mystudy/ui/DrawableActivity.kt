package com.example.mystudy.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.mystudy.R
import com.example.mystudy.widgets.CustomDrawable
import kotlinx.android.synthetic.main.activity_drawable.*

class DrawableActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawable)
    }

    fun onClick(v: View) {
        button5.background = CustomDrawable()
    }
}
