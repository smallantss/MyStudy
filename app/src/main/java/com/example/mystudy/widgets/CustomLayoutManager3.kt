package com.example.mystudy.widgets

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.example.mystudy.utils.L
import kotlin.math.max

/**
 * 实现流式布局
 */
class CustomLayoutManager3 : RecyclerView.LayoutManager() {

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT)
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        if (itemCount<=0){
            detachAndScrapAttachedViews(recycler)
            return
        }
        detachAndScrapAttachedViews(recycler)
        //布局children
        val maxW = getHorizontalSize()
        l("rvW->$maxW")
        //加上分割线的宽度
        var offsetX = 0
        var offsetY = 0
        var preChildH = 0
        for (i in 0 until itemCount){
            val child = recycler?.getViewForPosition(i)!!
            addView(child)
            measureChildWithMargins(child,0,0)
            val childW = getDecoratedMeasuredWidth(child)
            val childH = getDecoratedMeasuredHeight(child)
            if (offsetX+childW>maxW){
                //换行
                offsetX = 0
                offsetY+=preChildH
                layoutDecorated(child,offsetX,offsetY,offsetX+childW,offsetY+childH)
                l("换行子:$i,l->$offsetX,t->$offsetY,r->${offsetX+childW},b->${offsetY+childH}")
                offsetX+=childW
            }else{
                //同一行
                layoutDecorated(child,offsetX,offsetY,offsetX+childW,offsetY+childH)
                l("同行子:$i,l->$offsetX,t->$offsetY,r->${offsetX+childW},b->${offsetY+childH}")
                offsetX+=childW
                preChildH = childH
            }
        }

    }

    //获取父View的宽度
    private fun getHorizontalSize() :Int{
        return width - paddingLeft - paddingRight
    }

    fun l(s:String){
        L.e("CustomManager3",s)
    }

    override fun canScrollVertically(): Boolean {
        return true
    }

    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        //上往下滑 dy<0
        //下往上滑 dy>0
        offsetChildrenVertical(-dy)
        return dy
    }
}