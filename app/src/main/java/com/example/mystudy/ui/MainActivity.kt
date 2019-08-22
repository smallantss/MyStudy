package com.example.mystudy.ui

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.dnbus.DnBus
import com.example.dnbus.Subscribe
import com.example.dnbus.ThreadMode
import com.example.mystudy.R
import com.example.mystudy.utils.ScreenDensityUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ScreenDensityUtils.initData(this,application)

        DnBus.getInstance().register(this)

        ObjectAnimator.ofFloat(tv,"translationX",0f,10f,100f)
                .setDuration(200)
                .start()

    }

    fun onClick(view:View){
        DnBus.getInstance().post("aaaaa")
        startActivity(Intent(this, MyIntentService::class.java))
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun getMessage(s:String){
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show()
    }
}
