package com.example.mystudy.widgets

import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import com.example.mystudy.utils.L

class CustomLayoutManager : RecyclerView.LayoutManager() {

    val TAG= "CustomLayoutManager"
    //1.设置Item的布局参数，就是RecyclerView的子item的LayoutParams
    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams? {
        //直接子View决定自己的宽高
        return RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT)
    }

    //内容的高度
    var mTotalHeight = 0
    //2.对Item进行布局
    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        super.onLayoutChildren(recycler, state)
        //遍历所有的子View
        var offsetY = 0
        //itemCount = 50  总数
        var measuredHeight = 0
        L.e(TAG, itemCount.toString())
        for (i in 0 until itemCount){
            val view = recycler?.getViewForPosition(i)
            //添加测量子View并摆放
            addView(view)
            measureChildWithMargins(view!!,0,0)
            //获取的是item+decoration的总高度
            val measuredWidth = getDecoratedMeasuredWidth(view)
            measuredHeight = getDecoratedMeasuredHeight(view)
            layoutDecorated(view,0,offsetY,measuredWidth,measuredHeight+offsetY)
            offsetY+=measuredHeight
        }
        mTotalHeight = Math.max(offsetY,getVerticalSpace())
        L.e(TAG,"rvH->${getVerticalSpace()},子item总高度->$offsetY,itemH->$measuredHeight}")
    }

    //RecyclerView竖直方向内容高度 = 自己高度 - paddingTop - paddingBottom
    private fun getVerticalSpace():Int{
        return height-paddingBottom - paddingTop
    }

    //3.给Item添加滑动
    override fun canScrollVertically(): Boolean {
        return true
    }

//    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
//        L.e(TAG,"dy->$dy")
//        //dy表示手指在屏幕上每次滑动的位移
//        //上滑时，dy>0,因为上滑需要Item的y轴减少，所以需要减去dy
//        //下滑时，dy<0
//        offsetChildrenVertical(-dy)
//        return dy
//    }

    //保存所有移动过的dy
    var sumDy = 0
    //4.增加滑动限制，顶部不能上滑 底部不能下滑
    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        L.e(TAG,"dy->$dy")
        var moveY = dy
        //上滑时，dy>0
        //下滑时，dy<0
        if (sumDy+dy<0){
            //到顶限制：所有的dy累加，如果<0表示已经到达顶部
            moveY = -sumDy
        }else if (sumDy+dy>mTotalHeight - getVerticalSpace()){
            //到底限制：如果所有Item总高度和累计的dy-屏幕高度？相同
            moveY = mTotalHeight - getVerticalSpace() - sumDy
        }

        sumDy+=moveY
        //平移所有的Item
        offsetChildrenVertical(-moveY)
        return dy
    }
}