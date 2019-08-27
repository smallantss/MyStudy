package com.example.mystudy.widgets

import android.graphics.*
import android.graphics.drawable.Drawable

class CustomDrawable: Drawable() {

    private val mPaint = Paint().apply {
        isAntiAlias = true
        isDither = true
        color = Color.parseColor("#FF0000")
    }

    //绘制
    override fun draw(canvas: Canvas) {
        val rect = bounds
        //获取中心点
        val cx = rect.exactCenterX()
        val cy = rect.exactCenterY()
        //画⚪
        canvas.drawCircle(cx,cy,Math.min(cx,cy),mPaint)
    }

    override fun setAlpha(alpha: Int) {
        mPaint.alpha = alpha
        invalidateSelf()
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        mPaint.colorFilter = colorFilter
        invalidateSelf()
    }
}