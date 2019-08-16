package com.example.mystudy

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.Toast
import com.example.mystudy.utils.L
import kotlinx.android.synthetic.main.activity_drag.*
import kotlinx.android.synthetic.main.layout_float_window.view.*

class DragActivity : AppCompatActivity() {


    private lateinit var wm: WindowManager
    var viewX = 0f
    var viewY = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_drag)
//        touch1()
        touch2()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:$packageName"))
                startActivityForResult(intent, 5)
            }else{
                initWm()
            }
        }
        myView.post {
            viewX = myView.x
            viewY = myView.y
            L.e("TAG","x->$viewX，y->$viewY")
        }
    }

    private fun touch2() {
        var downX = 0f
        var moveX = 0f
        var w = 0
        /*view.setOnTouchListener { v, event ->
            w = v.width
            L("onTouch:$w")
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    downX = event.x
                    L("downX:$downX")
                    return@setOnTouchListener true
                }
                MotionEvent.ACTION_MOVE -> {
                    moveX = event.x - downX
                    L("MOVE:${event.x},$downX")
                    L("moveX:$moveX")
                    v.translationX += moveX

                    when(v.translationX/w){
                        in 0.1..0.2->showToast("0.1")
                        in 0.2..0.4->showToast("0.2")
                        in 0.4..0.8->showToast("0.4")
                        in 0.8..1.0->showToast("1")
                    }
//                    v.offsetLeftAndRight(moveX.toInt())
//                    v.layout((v.left + moveX).toInt(), v.top, (v.right + moveX).toInt(), v.bottom)
                }
            }
            return@setOnTouchListener super.onTouchEvent(event)
        }*/
    }

    private fun initWm(){
        val mWindowParams = WindowManager.LayoutParams().apply {
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
            }
            format = 1
//            gravity = Gravity.LEFT or Gravity.TOP
            x = 50
            y = 100
        }
        var mWDownX = 0f
        var mWDownY = 0f
        var mMoveX = 0f
        var mMoveY = 0f
        var eventY = 0f
        val windowView = View.inflate(this, R.layout.layout_float_window, null)
        windowView.setOnTouchListener { v, event ->
            when(event.action){
                MotionEvent.ACTION_DOWN->{
                    mWDownX = event.rawX
                    mWDownY = event.rawY
                    eventY = event.y
                    L.e("TAG","downX->${event.x},downY->${event.y},")
                    L.e("TAG","down l->${v.left}，t->${v.top},r->${v.right},b->${v.bottom}")
                    return@setOnTouchListener true
                }
                MotionEvent.ACTION_MOVE->{
                    L.e("TAG","moveX->${event.x},moveY->${event.y},")
                    val curX = event.rawX
                    val curY = event.rawY
                    mMoveX = curX - mWDownX
                    mMoveY = curY - mWDownY
                    mWindowParams.x += mMoveX.toInt()
                    mWindowParams.y += mMoveY.toInt()
                    wm.updateViewLayout(windowView, mWindowParams)
                    mWDownX = curX
                    mWDownY = curY
                    L.e("TAG","move l->${v.left}，t->${v.top},r->${v.right},b->${v.bottom}")


                    if (curY+(v.height - eventY)>viewY){
                        Toast.makeText(this,"进去了",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this,"出去了",Toast.LENGTH_SHORT).show()
                    }
                }
            }
            return@setOnTouchListener super.onTouchEvent(event)
        }
        windowView.tv0.setOnClickListener {
            startActivity(Intent(this@DragActivity,FragmentActivity::class.java))
        }
        windowView.tv2.setOnClickListener {
            wm.removeView(windowView)
        }
        val spec = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.AT_MOST)
        windowView.measure(spec,spec)
        wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.addView(windowView, mWindowParams)
    }

    private fun showToast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    private fun touch1() {
        var moveX = 0f
        var lastX = 0f
        /*view.setOnTouchListener { v, event ->
            var x = event.x
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastX = x
                    L("downX:$lastX")
                    return@setOnTouchListener true
                }
                MotionEvent.ACTION_MOVE -> {
                    moveX = x - lastX
                    L("moveX:$moveX")
                    //              1.      v.translationX+=moveX
                    //                    v.offsetLeftAndRight(moveX.toInt())
                    v.layout((v.left + moveX).toInt(), v.top, (v.right + moveX).toInt(), v.bottom)
                }
            }
            return@setOnTouchListener super.onTouchEvent(event)
        }*/
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 5) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    Toast.makeText(this,"开启悬浮框权限啊",Toast.LENGTH_SHORT).show()
                }else{
                    initWm()
                }
            }
        }
    }

}
