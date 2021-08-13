package com.example.mystudy.widgets

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import com.example.mystudy.loge

class MyTextView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyleAttr) {

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        loge("onDraw")
    }
}