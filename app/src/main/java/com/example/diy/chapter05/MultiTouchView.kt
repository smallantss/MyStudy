package com.example.diy.chapter05

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class MultiTouchView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val point = Point()
    private var secondPoint = false
    private val paint = Paint().apply {
        color = Color.WHITE
        isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return
        canvas.drawColor(Color.GREEN)
        if (secondPoint) {
            canvas.drawCircle(point.x.toFloat(), point.y.toFloat(), 50f, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return super.onTouchEvent(event)
        val index = event.actionIndex
        when (event.actionMasked) {
            //第一根手指按下是ACTION_DOWN，第二根手指按下是ACTION_POINTER_DOWN
            MotionEvent.ACTION_POINTER_DOWN -> {
                if (event.getPointerId(index) == 1) {
                    //找出第2根手指
                    secondPoint = true
                    point.set(event.x.toInt(), event.y.toInt())
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (secondPoint) {
                    val pointerIndex = event.findPointerIndex(1)
                    point.set(event.getX(pointerIndex).toInt(), event.getY(pointerIndex).toInt())
                }
            }
            MotionEvent.ACTION_POINTER_UP -> {
                if (event.getPointerId(index) == 1) {
                    secondPoint = false
                }
            }
            MotionEvent.ACTION_UP -> {
                secondPoint = false
            }
        }
        invalidate()
        return true
    }

}