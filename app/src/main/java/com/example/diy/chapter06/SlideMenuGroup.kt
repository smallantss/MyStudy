package com.example.diy.chapter06

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.customview.widget.ViewDragHelper
import kotlin.math.min

class SlideMenuGroup @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private lateinit var dragHelper: ViewDragHelper

    init {
        dragHelper = ViewDragHelper.create(this, 1.0f, object : ViewDragHelper.Callback() {
            override fun tryCaptureView(child: View, pointerId: Int): Boolean {
                return child == mMainView
            }

            override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
                if (left > 0) {
                    return min(left, mMenuViewWidth)
                }
                return 0
            }

            override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
                super.onViewPositionChanged(changedView, left, top, dx, dy)
                //移动的百分比
                val percent = changedView.left.toFloat() / mMenuViewWidth
                executeAnimation(percent)
            }

            override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
                if (releasedChild.left < mMenuViewWidth / 2) {
                    //关闭
                    dragHelper.smoothSlideViewTo(releasedChild, 0, 0)
                } else {
                    //打开
                    dragHelper.smoothSlideViewTo(releasedChild, mMenuViewWidth, 0)
                }
                invalidate()
            }

        })
    }

    private fun executeAnimation(percent: Float) {
        mMainView.scaleX = (1 - percent * 0.2f)
        mMainView.scaleY = (1 - percent * 0.2f)
        mMenuView.scaleX = 0.5f * percent + 0.5f
        mMenuView.scaleY = 0.5f * percent + 0.5f
        mMenuView.translationX = (-mMenuViewWidth / 2 + mMenuViewWidth / 2 * percent)
    }

    override fun computeScroll() {
        super.computeScroll()
        if (dragHelper.continueSettling(true)) {
            invalidate()
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        ev ?: return super.onInterceptTouchEvent(ev)
        return dragHelper.shouldInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return super.onTouchEvent(event)
        dragHelper.processTouchEvent(event)
        return true
    }

    private lateinit var mMainView: View
    private lateinit var mMenuView: View

    private var mMenuViewWidth = 500


    fun setView(main: View, mainParams: LayoutParams, menu: View, menuParams: LayoutParams) {
        mMenuView = menu
        addView(menu, menuParams)
        mMenuViewWidth = menuParams.width

        mMainView = main
        addView(main, mainParams)

    }


}