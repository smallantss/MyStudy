package com.example.mystudy.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.example.mystudy.BuildConfig
import com.example.mystudy.R
import com.example.mystudy.utils.LogUtils

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
    }
    private val rect = RectF()

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShadowView)
        typedArray.apply {
            //四舍五入获取px   offset 忽略小数获取px
            mDx = getDimensionPixelSize(R.styleable.ShadowView_shadowDx, 0)
            mDy = getDimensionPixelSize(R.styleable.ShadowView_shadowDy, 0)
            mShadowRadius = getDimensionPixelSize(R.styleable.ShadowView_shadowRadius, 0)
            mRadius = getDimensionPixelSize(R.styleable.ShadowView_radius, 0)
            mShadowColor = getColor(R.styleable.ShadowView_shadowColor, Color.BLACK)
            LogUtils.e("mDx->$mDx,mDy->$mDy,mShadowRadius->$mShadowRadius,mRadius->$mRadius")
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

    /**
     * 使用Paint.setShadowLayer
     * 阴影就等于在绘制的内容下面又绘制了一层，我们可以通过设置阴影的radius让其变得模糊
     * 因此，总宽 = 内容宽 + radius模糊半径*2
     */
    override fun onDraw(canvas: Canvas?) {
        //绘制出边界
        if (BuildConfig.DEBUG) {
            canvas?.drawRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), Paint().apply {
                style = Paint.Style.STROKE
                isAntiAlias = true
                strokeWidth = 1f
                color = Color.BLACK
            })
        }
        val paddingL = paddingLeft
        val paddingT = paddingTop
        val paddingR = paddingRight
        val paddingB = paddingBottom
        //绘制的内容  阴影+内容宽 = 总宽
        rect.set(paddingL + mShadowRadius.toFloat(),paddingT+mShadowRadius.toFloat(), (measuredWidth - mShadowRadius - paddingR).toFloat(), (measuredHeight - mShadowRadius - paddingB).toFloat())
        //设置阴影的属性
        mPaint.setShadowLayer(mShadowRadius.toFloat(), mDx.toFloat(), mDy.toFloat(), mShadowColor)
        mPaint.style = Paint.Style.STROKE
        mPaint.color = mShadowColor
        canvas?.drawRoundRect(rect, mRadius.toFloat(), mRadius.toFloat(), mPaint)
    }


    /**
     * 使用Paint.setMaskFilter
     */
//    override fun onDraw(canvas: Canvas?) {
//        //绘制出边界
//        if (BuildConfig.DEBUG) {
//            canvas?.drawRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), Paint().apply {
//                style = Paint.Style.STROKE
//                isAntiAlias = true
//                strokeWidth = 1f
//                color = Color.BLACK
//            })
//        }
//        val paddingL = paddingLeft
//        val paddingT = paddingTop
//        val paddingR = paddingRight
//        val paddingB = paddingBottom
//        mPaint.color = Color.RED
//        mPaint.maskFilter = BlurMaskFilter(mShadowRadius.toFloat(),BlurMaskFilter.Blur.SOLID)
//        rect.set(paddingL+ mShadowRadius.toFloat(),paddingT+mShadowRadius.toFloat(),(measuredWidth - mShadowRadius- paddingR).toFloat(),(measuredHeight - mShadowRadius - paddingB).toFloat())
//        canvas?.drawRoundRect(rect,mRadius.toFloat(), mRadius.toFloat(),mPaint)
//    }


}