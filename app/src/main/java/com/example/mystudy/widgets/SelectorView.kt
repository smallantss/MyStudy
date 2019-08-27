package com.example.mystudy.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class SelectorView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    val paint = Paint().apply {
        isDither = true
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.RED
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            it.drawCircle((measuredWidth/2).toFloat(), (measuredHeight/2).toFloat(),50f,paint)
        }
    }

    var downX = 0f
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            when(it.action){
                MotionEvent.ACTION_DOWN->{
                    downX = it.x
                }
                MotionEvent.ACTION_MOVE->{
                    val moveX = it.x
                    val deltaX = moveX - downX
                    scrollBy(-deltaX.toInt(),0)
                    downX = it.x
                }
            }
        }
        return true
    }
}