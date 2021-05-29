package com.example.diy.chapter06

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.Scroller

class ScrollLinearLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private lateinit var scroller: Scroller

    init {
        scroller = Scroller(context)
    }

    fun start() {
        //(0,0)->(-100,0)
        scroller.startScroll(0, 0, 100, 0, 500)
        invalidate()
    }

    override fun computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.currX, scroller.currY)
            invalidate()
        }
    }

    fun reset(){
//        (100,0)->(0,0) startX代表偏移量的位置
        scroller.startScroll(100, 0, -100, 0, 500)
        invalidate()
    }
}