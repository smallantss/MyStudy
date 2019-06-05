package com.example.mystudy

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.example.mystudy.utils.L
import kotlinx.android.synthetic.main.activity_drag.*

class DragActivity : AppCompatActivity(){

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
        when(event?.action){
            MotionEvent.ACTION_DOWN->{
                downX = event.x
                return true
            }
            MotionEvent.ACTION_MOVE->{
                canvas.drawLine(10f,10f,200f,200f,paint)
            }
            MotionEvent.ACTION_UP->{

            }
        }
        return super.onTouchEvent(event)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drag)
//        touch1()
        touch2()

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

    private fun showToast(s:String) {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show()
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
