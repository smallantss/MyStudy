package com.example.mystudy.ui

//import com.example.dnbus.DnBus
//import com.example.dnbus.Subscribe
//import com.example.dnbus.ThreadMode
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mystudy.R
import com.example.mystudy.utils.ScreenDensityUtils
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ScreenDensityUtils.initData(this,application)

//        DnBus.getInstance().register(this)

        ObjectAnimator.ofFloat(tv,"translationX",0f,10f,100f)
                .setDuration(200)
                .start()

    }

    fun onClick(view:View){
//        DnBus.getInstance().post("aaaaa")
        startActivity(Intent(this, MyIntentService::class.java))
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun getMessage(s:String){
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show()
    }
}
