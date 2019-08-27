package com.example.mystudy.widgets

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.util.TypedValue.COMPLEX_UNIT_DIP
import android.util.TypedValue.COMPLEX_UNIT_SP
import android.widget.TextView

class MsgView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {

    val paint = Paint().apply {
        isAntiAlias = true
        isDither = true
        color = Color.RED
        style = Paint.Style.FILL
    }

    var fmInt:Paint.FontMetricsInt

    val textPaint = Paint().apply {
        isAntiAlias = true
        isDither = true
        color = Color.WHITE
        style = Paint.Style.FILL
        textSize = TypedValue.applyDimension(COMPLEX_UNIT_SP, 12f, context.resources.displayMetrics)
        fmInt = fontMetricsInt
    }

    val rect = RectF()

    val corners = TypedValue.applyDimension(COMPLEX_UNIT_DIP, 12f, context.resources.displayMetrics)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        Log.e("TAG","mW->$measuredWidth,mH->$measuredHeight")
        if (measuredWidth <= measuredHeight){
            setMeasuredDimension(measuredHeight,measuredHeight)
        }

    }


    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            val textW = textPaint.measureText(text.toString())
            val x = measuredWidth/2 - textW/2
            val y = measuredHeight/2+(fmInt.bottom-fmInt.top)/2-fmInt.bottom
            if (measuredWidth > measuredHeight) {
                //宽大于高，椭圆
                rect.set(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
                it.drawRoundRect(rect, corners, corners, paint)
            } else {
                //圆
                it.drawCircle((measuredWidth / 2).toFloat(), (measuredHeight / 2).toFloat(), (measuredHeight / 2).toFloat(), paint)
            }
            it.drawText(text,0,text.length,x,y.toFloat(),textPaint)

        }
    }
}