package com.example.diy.chapter05

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatTextView
import kotlin.math.abs
import kotlin.math.sqrt

class TouchScaleTextView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var oldDis = 0f
    private var mTextSize = 0f
    //手指个数
    private var count = 0

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return super.onTouchEvent(event)
        if (mTextSize == 0f) {
            mTextSize = textSize
        }
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                oldDis = 0f
                count = 1
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                //获取两根手指间的距离
                oldDis = spacing(event)
                count++
            }
            MotionEvent.ACTION_MOVE -> {
                if (count>=2){
                    //大于两根手指，计算距离
                    val newDis = spacing(event)
                    //过滤小的移动
                    if (abs(newDis-oldDis)>50){
                        zoom(newDis/oldDis)
                        oldDis = newDis
                    }
                }
            }
            MotionEvent.ACTION_POINTER_UP -> {
                count--
            }
            MotionEvent.ACTION_UP -> {
                count = 0
            }
        }

        return true
    }

    private fun zoom(f: Float) {
        mTextSize*=f
        setTextSize(TypedValue.COMPLEX_UNIT_PX,mTextSize)
    }

    private fun spacing(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return sqrt(x * x + y * y)
    }

}