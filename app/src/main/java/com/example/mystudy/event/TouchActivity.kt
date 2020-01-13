package com.example.mystudy.event

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mystudy.R
import kotlinx.android.synthetic.main.activity_touch.*

/**
 * 测试事件分发的Activity
 */
class TouchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_touch)
        root.setTag("ROOT")
        parentView.setTag("PARENT")
        parentView2.setTag("PARENT2")
        parentView3.setTag("PARENT3")
    }
}
