package com.example.mystudy.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import com.example.mystudy.utils.L

class MyView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    override fun onDraw(canvas: Canvas?) {
        L.e("MyView","onDraw")
        super.onDraw(canvas)
        canvas?.apply {
            rotate(90f)
            drawColor(Color.RED)
        }
    }

    override fun dispatchDraw(canvas: Canvas?) {
        L.e("MyView","dispatchDraw")
        super.dispatchDraw(canvas)
    }
}