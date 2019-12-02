package com.example.mystudy.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mystudy.R
import com.example.mystudy.designpattern.builder.DefaultNavigationBar
import kotlinx.android.synthetic.main.activity_builder.*

class BuilderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_builder)

        DefaultNavigationBar.Builder(this, root)
                .setLeftText("CCCCCCCCC")
                .hideLeft()
                .create()
    }
}
