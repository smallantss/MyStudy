package com.example.mystudy.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.mystudy.R
import com.example.mystudy.loge
import kotlinx.android.synthetic.main.activity_diy.*

class DiyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diy)
        Handler().postDelayed({
            loge("w:${root.layoutParams.width}")
            loge("h:${root.layoutParams.height}")
            loge("root:${root.width},${root.height}")
            loge("tvParams:${view.layoutParams.width},${view.layoutParams.height}")
            loge("tv:${view.width},${view.height}")
        }, 2000)
    }
}