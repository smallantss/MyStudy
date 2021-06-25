package com.example.diy.chapter06

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.mystudy.R
import kotlinx.android.synthetic.main.activity_slide_menu.*

class SlideMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slide_menu)
        val view = LayoutInflater.from(this).inflate(R.layout.slide_layout, null, false)
        val menu = LayoutInflater.from(this).inflate(R.layout.menu_layout, null, false)
        val menuWidth = resources.getDimensionPixelSize(R.dimen.menu_width)
        slideMenuGroup.setView(view, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT),
                menu, FrameLayout.LayoutParams(menuWidth, FrameLayout.LayoutParams.MATCH_PARENT))
    }
}