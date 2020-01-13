package com.example.mystudy.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.example.mystudy.utils.LogUtils

class FlowLayout2 @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    //计算所有子View宽高并计算出自己的宽高
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val wMode = MeasureSpec.getMode(widthMeasureSpec)
        val wSize = MeasureSpec.getSize(widthMeasureSpec)
        val hMode = MeasureSpec.getMode(heightMeasureSpec)
        val hSize = MeasureSpec.getSize(heightMeasureSpec)

        measureChildren(widthMeasureSpec, heightMeasureSpec)

        var sumX = 0
        var sumY = 0
        var maxW = 0

        var preViewH = 0

        //保存当前行的宽高
        var curLineW = 0
        var curLineH = 0

        //保存当前行的最大宽高，设置viewGroup要以最大的宽为准
        var lineMaxW = 0
        //上一行宽度
        var preLineW = 0

        var lineMaxH = 0
        var line = 0

        for (i in 0 until childCount){
            val child = getChildAt(i)
            val params = child.layoutParams as MarginLayoutParams
            val childW = child.measuredWidth
            val childH = child.measuredHeight

            if (childW+curLineW>wSize){
                //换行 记录最大的宽高
                lineMaxW = Math.max(curLineW,preLineW)
                sumY+=lineMaxH
                line++
            }else{
                //当前行
                curLineW+=childW
                lineMaxH = Math.max(childH,preViewH)
                preViewH = childH
            }
        }
        l("父高度->$wSize,总高度->$sumY,最大的宽->$lineMaxW,行数->$line")
        setMeasuredDimension(wSize,sumY)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        l("父width->$measuredWidth,height->$measuredHeight")
        var offsetX = 0
        var offsetY = 0
        var preChildH = 0
        for (i in 0 until childCount){
            val child = getChildAt(i)
//            val params = child.layoutParams as MarginLayoutParams
            val childW = child.measuredWidth
            val childH = child.measuredHeight
            if (offsetX+childW>measuredWidth){
                //换行
                offsetX = 0
                offsetY+=preChildH
                child.layout(offsetX,offsetY,offsetX+childW,offsetY+childH)
                offsetX+=childW
                l("换行子:$i,l->$offsetX,t->$offsetY,r->${offsetX+childW},b->${offsetY+childH}")
            }else{
                //同一行
                child.layout(offsetX,offsetY,offsetX+childW,offsetY+childH)
                l("同行子:$i,l->$offsetX,t->$offsetY,r->${offsetX+childW},b->${offsetY+childH}")
                offsetX+=childW
                preChildH = childH
            }

        }
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    fun getHSize(): Int {
        return width - paddingLeft - paddingRight
    }

    fun l(s:String){
        LogUtils.e("FlowLayout",s)
    }
}