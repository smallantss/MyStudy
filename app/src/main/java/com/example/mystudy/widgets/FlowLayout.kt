package com.example.mystudy.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup

class FlowLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    //计算所有子View宽高并计算出自己的宽高
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val wMode = MeasureSpec.getMode(widthMeasureSpec)
        val wSize = MeasureSpec.getSize(widthMeasureSpec)
        val hMode = MeasureSpec.getMode(heightMeasureSpec)
        val hSize = MeasureSpec.getSize(heightMeasureSpec)

        measureChildren(widthMeasureSpec, heightMeasureSpec)

        var lHeight = 0
        var rHeight = 0
        var tWidth = 0
        var bWidth = 0

        val child0 = getChildAt(0)
        val params0 = child0.layoutParams as MarginLayoutParams
        val child0W = child0.measuredWidth
        val child0H = child0.measuredHeight

        val child2 = getChildAt(2)
        val params2 = child2.layoutParams as MarginLayoutParams
        val child2W = child2.measuredWidth
        val child2H = child2.measuredHeight

        val child1 = getChildAt(1)
        val params1 = child0.layoutParams as MarginLayoutParams
        val child1W = child1.measuredWidth
        val child1H = child1.measuredHeight

        val child3 = getChildAt(3)
        val params3 = child3.layoutParams as MarginLayoutParams
        val child3W = child3.measuredWidth
        val child3H = child3.measuredHeight

        lHeight = child0H + params0.topMargin + params0.bottomMargin + child2H + params2.topMargin + params2.bottomMargin

        rHeight = child1H + params1.topMargin + params1.bottomMargin + child3H + params3.topMargin + params3.bottomMargin

        tWidth = child0W + params0.leftMargin + params0.rightMargin + child1W + params1.leftMargin + params1.rightMargin

        bWidth = child2W + params2.leftMargin + params2.rightMargin + child3W + params3.leftMargin + params3.rightMargin

        var maxW = Math.max(tWidth, bWidth)
        var maxH = Math.max(lHeight, rHeight)

        if (wMode == MeasureSpec.EXACTLY) {
            maxW = wSize
        }
        if (hMode == MeasureSpec.EXACTLY) {
            maxH = hSize
        }
        setMeasuredDimension(maxW, maxH)

    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

        val child0 = getChildAt(0)
        val child1 = getChildAt(1)
        val child2 = getChildAt(2)
        val child3 = getChildAt(3)

        var params = child0.layoutParams as MarginLayoutParams
        child0.layout(params.leftMargin, params.topMargin,
                params.leftMargin + child0.measuredWidth,
                params.topMargin + child0.measuredHeight)

        params = child1.layoutParams as MarginLayoutParams
        child1.layout(width - params.rightMargin - child1.measuredWidth, params.topMargin,
                width - params.rightMargin - child1.measuredWidth+child1.measuredWidth,
                params.topMargin + child1.measuredHeight)

        params = child2.layoutParams as MarginLayoutParams
        child2.layout(params.leftMargin, height - params.bottomMargin-child2.measuredHeight,
                params.leftMargin+child2.measuredWidth,
                height - params.bottomMargin-child2.measuredHeight + child2.measuredHeight)

        params = child3.layoutParams as MarginLayoutParams
        child3.layout(width - params.rightMargin - child3.measuredWidth, height - params.bottomMargin-child3.measuredHeight,
                width - params.rightMargin - child3.measuredWidth+child3.measuredWidth,
                height - params.bottomMargin-child3.measuredHeight + child3.measuredHeight)


//        val count = childCount
//        val parentW = getHSize()
//        var offsetX = 0
//        var offsetY = 0
//        for (i in 0 until count){
//            val child = getChildAt(i)
//            measureChild(child,0,0)
//            val w = child.width
//            val h = child.height
//            if (offsetX+w>parentW){
//                offsetY+=h
//                offsetX = 0
//            }
//            child.layout(offsetX,offsetY,child.width,offsetY+child.height)
//            offsetX+=child.width
//        }
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    fun getHSize(): Int {
        return width - paddingLeft - paddingRight
    }
}