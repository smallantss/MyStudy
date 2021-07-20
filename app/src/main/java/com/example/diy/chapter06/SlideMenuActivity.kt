package com.example.diy.chapter06

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.*
import com.example.mystudy.R
import com.example.mystudy.mvvm.viewmodel.MyViewModel
import com.example.mystudy.ui.loge
import com.example.mystudy.utils.LifecycleUtil
import com.example.mystudy.utils.SingleLiveData
import com.example.mystudy.utils.SingleLiveData2
import kotlinx.android.synthetic.main.activity_slide_menu.*
import kotlinx.coroutines.*

class SlideMenuActivity : AppCompatActivity() {

    val liveData = MutableLiveData<Int>()

    val viewModel:MyViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slide_menu)

        val view = LayoutInflater.from(this).inflate(R.layout.slide_layout, null, false)
        val menu = LayoutInflater.from(this).inflate(R.layout.menu_layout, null, false)
        val menuWidth = resources.getDimensionPixelSize(R.dimen.menu_width)
        slideMenuGroup.setView(view, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT),
                menu, FrameLayout.LayoutParams(menuWidth, FrameLayout.LayoutParams.MATCH_PARENT))
        viewModel.a()
        lifecycle.addObserver(LifecycleUtil())

    }

    override fun onResume() {
        super.onResume()
        loge("onResume")
        liveData.value = 10
        liveData.value = 100
        liveData.observeForever {
            loge("observeForever:$it")
        }
        liveData.observe(this@SlideMenuActivity, Observer {
            loge("observe:$it")
        })

    }

    override fun onStart() {
        super.onStart()
        loge("onStart")
    }
//
//    override fun onPause() {
//        super.onPause()
//        loge("onPause")
//    }
//
//    override fun onStop() {
//        super.onStop()
//        loge("onStop")
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        loge("onDestroy")
//    }
}