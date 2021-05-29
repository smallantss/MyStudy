package com.example.diy.chapter01

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Matrix
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.BounceInterpolator
import android.widget.LinearLayout

class ClockViewGroup @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var centerX: Float = 0f
    private var centerY: Float = 0f
    private val mMatrix = Matrix()
    private val mCamera = Camera()
    private var mRotateX = 0f
    private var mRotateY = 0f

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = w / 2f
        centerY = h / 2f
    }

    override fun dispatchDraw(canvas: Canvas?) {
        mMatrix.reset()
        mCamera.save()
        mCamera.rotateX(mRotateX)
        mCamera.rotateY(mRotateY)

        mCamera.getMatrix(mMatrix)
        mCamera.restore()

        mMatrix.preTranslate(-centerX, -centerY)
        mMatrix.postTranslate(centerX, centerY)

        canvas?.save()
        canvas?.setMatrix(mMatrix)
        super.dispatchDraw(canvas)
        canvas?.restore()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event?.x ?: 0f
        val y = event?.y ?: 0f
        when (event?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                rotateCanvas(x, y)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                rotateCanvas(x, y)
            }
            MotionEvent.ACTION_UP -> {
                reset()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun reset() {
        val propertyNameX = "mRotateX"
        val propertyNameY = "mRotateY"
        val holderRotateX = PropertyValuesHolder.ofFloat(propertyNameX, mRotateX, 0f)
        val holderRotateY = PropertyValuesHolder.ofFloat(propertyNameY, mRotateY, 0f)
        val anim = ValueAnimator.ofPropertyValuesHolder(holderRotateX, holderRotateY).apply {
            duration = 1000
            interpolator = BounceInterpolator()
            addUpdateListener {
                mRotateX = it.getAnimatedValue(propertyNameX) as Float
                mRotateY = it.getAnimatedValue(propertyNameY) as Float
                invalidate()
            }
            start()
        }
    }

    private fun rotateCanvas(x: Float, y: Float) {
        val dx = x - centerX
        val dy = y - centerY
        var percentX = dx / (width / 2)
        var percentY = dy / (height / 2)
        if (percentX > 1f) {
            percentX = 1f
        } else if (percentX < -1f) {
            percentX = -1f
        }
        if (percentY > 1f) {
            percentY = 1f
        } else if (percentY < -1f) {
            percentY = -1f
        }
        mRotateX = -percentY * 20
        mRotateY = percentX * 20
        postInvalidate()
    }
}