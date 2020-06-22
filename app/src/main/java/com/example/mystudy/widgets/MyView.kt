package com.example.mystudy.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.mystudy.utils.LogUtils

class MyView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    override fun onDraw(canvas: Canvas?) {
        LogUtils.e("MyView","onDraw")
        super.onDraw(canvas)
        canvas?.apply {
            rotate(90f)
            drawColor(Color.RED)
        }
    }

    override fun dispatchDraw(canvas: Canvas?) {
        LogUtils.e("MyView","dispatchDraw")
        super.dispatchDraw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN->{
                LogUtils.e("MyView","getX():${this.x},getY():${this.y}")
                LogUtils.e("MyView","getLeft():${this.left},getTop():${this.top},getRight():${this.right},getBottom():${this.bottom}")
                LogUtils.e("MyView","event.getX():${event.x},event.getY():${event.y}")
                return true
            }
            MotionEvent.ACTION_MOVE->{

            }
            MotionEvent.ACTION_UP->{

            }
        }
        return super.onTouchEvent(event)
    }
}