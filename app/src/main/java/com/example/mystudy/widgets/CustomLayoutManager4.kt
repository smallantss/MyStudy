package com.example.mystudy.widgets

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.example.mystudy.utils.L

class CustomLayoutManager4 : RecyclerView.LayoutManager() {

    val TAG = "CustomLayoutManager"
    //1.设置Item的布局参数，就是RecyclerView的子item的LayoutParams
    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams? {
        //直接子View决定自己的宽高
        return RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT)
    }

    //内容的高度
    var mTotalHeight = 0
    //每一个item的宽高
    var mItemW = 0
    var mItemH = 0
    //屏幕上可见的item个数
    var visibleCount = 0
    //保存所有Item的位置
    private val itemRects = ArrayList<Rect>()

    //2.对Item进行布局
    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        L.e(TAG, "onLayoutChildren->before detach $childCount")
        //没有Item
        if (itemCount == 0) {
            //剥离所有Item，清空屏幕
            detachAndScrapAttachedViews(recycler)
            return
        }
        detachAndScrapAttachedViews(recycler)
        L.e(TAG, "onLayoutChildren->after detach $itemCount")
        //申请一个HolderView,获取其宽高
        val itemView0 = recycler?.getViewForPosition(0)
        //先测量item才能获取宽高
        measureChildWithMargins(itemView0, 0, 0)
        mItemW = getDecoratedMeasuredWidth(itemView0)
        mItemH = getDecoratedMeasuredHeight(itemView0)

        //计算屏幕上可以显示多少个
//        visibleCount = Math.ceil(getVerticalSpace() / mItemH.toDouble()).toInt()
        visibleCount = getVerticalSpace() / mItemH
        L.e(TAG, "onLayoutChildren->vSpace->${getVerticalSpace()},mItemH->$mItemH," +
                "visibleCount $visibleCount,")

        //保存所有的item位置信息及子item总共的高度
        var rect: Rect?
        var offsetY = 0
        for (i in 0 until itemCount) {
            rect = Rect(0, offsetY, mItemW, mItemH + offsetY)
            itemRects.add(rect)
            offsetY += mItemH
        }

        //遍历屏幕上可见的个数
        for (i in 0 until visibleCount) {
            val view = recycler?.getViewForPosition(i)
            //因为detach了所有的item，所以要重新添加回
            addView(view)
            //addView之后一定要measure
            measureChildWithMargins(view, 0, 0)
            rect = itemRects[i]
            //layout
            layoutDecorated(view, rect.left, rect.top, rect.right, rect.bottom)
        }

        //获取最大高度
        mTotalHeight = Math.max(getVerticalSpace(), offsetY)

        /*//遍历所有的子View
        var offsetY = 0
        //itemCount = 50  总数
        var measuredHeight = 0
        L.e(TAG, itemCount.toString())
        for (i in 0 until itemCount) {
            val view = recycler?.getViewForPosition(i)
            //添加测量子View并摆放
            addView(view)
            measureChildWithMargins(view!!, 0, 0)
            //获取的是item+decoration的总高度
            val measuredWidth = getDecoratedMeasuredWidth(view)
            measuredHeight = getDecoratedMeasuredHeight(view)
            layoutDecorated(view, 0, offsetY, measuredWidth, measuredHeight + offsetY)
            offsetY += measuredHeight
        }
        mTotalHeight = Math.max(offsetY, getVerticalSpace())
        L.e(TAG, "rvH->${getVerticalSpace()},子item总高度->$offsetY,itemH->$measuredHeight}")*/
    }

    //RecyclerView竖直方向内容高度 = 自己高度 - paddingTop - paddingBottom
    private fun getVerticalSpace(): Int {
        return height - paddingBottom - paddingTop
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
    var travel = 0

    //4.增加滑动限制，顶部不能上滑 底部不能下滑
    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        L.e(TAG, "scrollVerticallyBy dy->$dy,childCount->$childCount,scrapCache->${recycler!!.scrapList.size}")
        if (childCount <= 0) {
            return dy
        }
        //这个才是item真正移动的距离
        travel = dy
        //1.判断滑动边界
        //上滑时，dy>0
        //下滑时，dy<0
        if (sumDy + dy < 0) {
            //到顶了，下滑限制：所有的dy累加，如果<0表示已经到达顶部
            //保证view最上方是到顶部
            travel = -sumDy
        } else if (sumDy + dy > mTotalHeight - getVerticalSpace()) {
            //到底了，上滑限制：如果所有Item总高度和累计的dy-屏幕高度？相同
            //保证view最下方是到底部
            travel = mTotalHeight - getVerticalSpace() - sumDy
        }

        //2.判断回收的item
        //需要遍历所有当前正在显示的Item，模拟移动travel后，看是不是还在屏幕上
        //当travel>0,下往上滑，将顶部item移除，所以需要判断当前的item是不是超过了上边界（y=0）
        //屏幕上从最后一个到第0个
        (childCount - 1).downTo(0).forEach {
            val child = getChildAt(it)
            if (travel > 0) {
                //下往上滑,回收屏幕上越界的View
                if (getDecoratedBottom(child) - travel < 0) {
                    removeAndRecycleView(child, recycler)
                }
            } else if (travel < 0) {
                //上往下滑，回收屏幕底部越界的View   Rv可显示的最底部位置
                if (getDecoratedTop(child) - travel > (height - paddingBottom)) {
                    removeAndRecycleView(child, recycler)
                }
            }
        }


        //3.判断显示的Item
        val visibleArea = getVisibleArea()
        if (travel >= 0) {
            //往上滑回收
            //获取屏幕上最后一个显示的View
            val view = getChildAt(childCount - 1)
            //获取屏幕上最后一个显示View的下一个View的索引
            val minPos = getPosition(view) + 1
            for (i in minPos until itemCount) {
                val nextRect = itemRects[i]
                if (Rect.intersects(visibleArea, nextRect)) {
                    val nextView = recycler?.getViewForPosition(i)
                    addView(nextView)
                    measureChildWithMargins(nextView, 0, 0)
                    layoutDecorated(nextView, nextRect.left, nextRect.top - sumDy,
                            nextRect.right, nextRect.bottom - sumDy)
                } else {
                    break
                }
            }
        } else {
            //下滑
            //获取屏幕上第一个显示的View
            val view = getChildAt(0)
            //屏幕上第一个显示的View的上一个View
            val maxPos = getPosition(view) - 1
            maxPos.downTo(0).forEach {
                val preRect = itemRects[it]
                if (Rect.intersects(visibleArea, preRect)) {
                    //获取HolderView
                    val preView = recycler?.getViewForPosition(it)
                    addView(preView, 0)
                    measureChild(preView, 0, 0)
                    layoutDecorated(preView, preRect.left, preRect.top - sumDy,
                            preRect.right, preRect.bottom - sumDy)
                } else {
                    return@forEach
                }
            }

        }

        sumDy += travel

        //平移所有的Item
        offsetChildrenVertical(-travel)
        return travel
    }

    //移动后屏幕显示的位置
    fun getVisibleArea(): Rect {
        return Rect(paddingLeft,
                paddingTop + sumDy + travel,
                paddingRight + width,
                getVerticalSpace() + sumDy + travel)
    }


}