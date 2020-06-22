package com.example.mystudy.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

class ShadowLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    val shadowColor = Color.RED
    private val mShadowRadius = 20

    init {
        setLayerType(View.LAYER_TYPE_SOFTWARE,null)
    }

    override fun dispatchDraw(canvas: Canvas?) {

        val paint = Paint()
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.setShadowLayer(mShadowRadius.toFloat(), 0f, 0f, shadowColor)
        val rect = RectF((paddingLeft + mShadowRadius).toFloat(), (paddingTop + mShadowRadius).toFloat(), (measuredWidth - paddingRight - mShadowRadius).toFloat(), (measuredHeight - paddingBottom - mShadowRadius).toFloat())
        canvas?.drawRoundRect(rect, 0.toFloat(), 0.toFloat(),paint)

        //外边缘的边框
        val aaa = RectF((paddingLeft + mShadowRadius).toFloat(), (paddingTop + mShadowRadius).toFloat(), (measuredWidth - paddingRight - mShadowRadius).toFloat(), (measuredHeight - paddingBottom - mShadowRadius).toFloat())
        canvas?.drawRoundRect(aaa,mShadowRadius.toFloat(), mShadowRadius.toFloat(), Paint().apply {
            isAntiAlias = true
            color = Color.BLUE
        })
        super.dispatchDraw(canvas)
    }
}