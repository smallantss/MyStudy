package com.example.mystudy.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import com.example.mystudy.BuildConfig
import com.example.mystudy.R
import com.example.mystudy.utils.L

/**
 * 带阴影的View
 */
class ShadowView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val mDx: Int
    private val mDy: Int
    private val mShadowColor: Int
    private val mShadowRadius: Int
    private val mRadius: Int
    private val mPaint: Paint = Paint().apply {
        style = Paint.Style.FILL
    }
    private val rect = RectF()

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShadowView)
        typedArray.apply {
            //四舍五入获取px   offset 忽略小数获取px
            mDx = getDimensionPixelSize(R.styleable.ShadowView_shadowDx, 0)
            mDy = getDimensionPixelSize(R.styleable.ShadowView_shadowDy, 0)
            mShadowRadius = getDimensionPixelSize(R.styleable.ShadowView_shadowRadius, 10)
            mRadius = getDimensionPixelSize(R.styleable.ShadowView_radius, 0)
            mShadowColor = getColor(R.styleable.ShadowView_shadowColor, Color.BLACK)
            L.e("mDx->$mDx,mDy->$mDy,mShadowRadius->$mShadowRadius,mRadius->$mRadius")
            rect.set(mShadowRadius.toFloat(), mShadowRadius.toFloat(), (width - mShadowRadius).toFloat(), (height - mShadowRadius).toFloat())
            recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val wMode = MeasureSpec.getMode(widthMeasureSpec)
        var wSize = MeasureSpec.getSize(widthMeasureSpec)
        val hMode = MeasureSpec.getMode(heightMeasureSpec)
        var hSize = MeasureSpec.getSize(heightMeasureSpec)
        if (wMode == MeasureSpec.AT_MOST) {
            wSize = 500
        }
        if (hMode == MeasureSpec.AT_MOST) {
            hSize = 500
        }
        setMeasuredDimension(wSize, hSize)
    }

    override fun onDraw(canvas: Canvas?) {
        measuredWidth
        //绘制出边界
        if (BuildConfig.DEBUG) {
            canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), Paint().apply {
                style = Paint.Style.STROKE
                isAntiAlias = true
                strokeWidth = 1f
                color = Color.MAGENTA
            })
        }
        if (background is ColorDrawable) {
            mPaint.color = (background as ColorDrawable).color
        } else {
            mPaint.color = Color.RED
        }
        //绘制的内容  阴影+内容宽 = 总宽
        //设置阴影的属性
        mPaint.setShadowLayer(mShadowRadius.toFloat(), mDx.toFloat(), mDy.toFloat(), mShadowColor)
        canvas?.drawRoundRect(rect, mRadius.toFloat(), mRadius.toFloat(), mPaint)
    }


}