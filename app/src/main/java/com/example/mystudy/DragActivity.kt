package com.example.mystudy

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.example.mystudy.utils.L
import kotlinx.android.synthetic.main.activity_drag.*
import kotlinx.android.synthetic.main.layout_float_window.view.*

class DragActivity : AppCompatActivity() {

    val paint = Paint().apply {
        isAntiAlias = true
        isDither = true
        color = Color.RED
        strokeWidth = 5f
        style = Paint.Style.FILL
    }
    val canvas = Canvas()

    var downX = 0f

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        L.e("onTouchEvent")
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                canvas.drawLine(10f, 10f, 200f, 200f, paint)
            }
            MotionEvent.ACTION_UP -> {

            }
        }
        return super.onTouchEvent(event)
    }

    private lateinit var wm: WindowManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drag)
//        touch1()
        touch2()

        val mWindowParams = WindowManager.LayoutParams().apply {
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_PHONE
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
        val windowView = View.inflate(this, R.layout.layout_float_window, null)
        windowView.setOnTouchListener { v, event ->
            when(event.action){
                MotionEvent.ACTION_DOWN->{
                    mWDownX = event.rawX
                    mWDownY = event.rawY
                    return@setOnTouchListener true
                }
                MotionEvent.ACTION_MOVE->{
                    val curX = event.rawX
                    val curY = event.rawY
                    mMoveX = curX - mWDownX
                    mMoveY = curY - mWDownY
                    mWindowParams.x += mMoveX.toInt()
                    mWindowParams.y += mMoveY.toInt()
                    wm.updateViewLayout(windowView, mWindowParams)
                    mWDownX = curX
                    mWDownY = curY
                }
            }
            return@setOnTouchListener super.onTouchEvent(event)
        }
        windowView.tv0.setOnClickListener {
            startActivity(Intent(this@DragActivity,FragmentActivity::class.java))
        }
        val spec = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.AT_MOST)
        windowView.measure(spec,spec)
        wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.addView(windowView, mWindowParams)

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

}
