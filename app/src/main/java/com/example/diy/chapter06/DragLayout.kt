package com.example.diy.chapter06

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.customview.widget.ViewDragHelper
import androidx.customview.widget.ViewDragHelper.EDGE_LEFT
import androidx.customview.widget.ViewDragHelper.EDGE_TOP
import com.example.mystudy.R
import com.example.mystudy.ui.loge
import kotlinx.android.synthetic.main.activity_scroller.view.*

class DragLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private lateinit var helper: ViewDragHelper

    init {
        //forParent就是要拖动的item的父控件，这里就是自己;1.0f敏感度越大越敏感；拦截到消息的回调，给用户操作item
        helper = ViewDragHelper.create(this, 1.0f, object : ViewDragHelper.Callback() {
            //拦截哪个控件的触摸事件 child当前触摸的对象 pointerId触摸该view的手指id true捕捉，有其他反馈 false不捕捉，无反馈
            override fun tryCaptureView(child: View, pointerId: Int): Boolean {
                loge("tryCaptureView")
                return true
            }

            //在view上横向移动的反馈 left移动到的left dx手指横向移动的距离 return代表子view的left移动到这里
            override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
                return left
            }

            //在view上纵向移动的反馈 top移动到的top dy手指纵向移动的距离
            override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
                return top
            }

            override fun getViewHorizontalDragRange(child: View): Int {
                return 100
            }

            override fun onEdgeTouched(edgeFlags: Int, pointerId: Int) {
                loge("onEdgeTouched:$edgeFlags")
                super.onEdgeTouched(edgeFlags, pointerId)
            }

            override fun onEdgeDragStarted(edgeFlags: Int, pointerId: Int) {
                loge("onDragStarted:$edgeFlags")
                super.onEdgeDragStarted(edgeFlags, pointerId)
                helper.captureChildView(view2, pointerId)
            }

            override fun onEdgeLock(edgeFlags: Int): Boolean {
                loge("onEdgeLock:$edgeFlags")
                return edgeFlags == EDGE_LEFT
            }

            override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
                loge("onViewReleased:${releasedChild.tag}")
                if (releasedChild==view2){
//                    helper.smoothSlideViewTo(view2, view1.left, view1.top)
//                    helper.settleCapturedViewAt(view1.left, view1.top)
                    helper.flingCapturedView(10,50,100,150)
                    invalidate()
                }
            }

        })
        helper.setEdgeTrackingEnabled(EDGE_LEFT or EDGE_TOP)
    }

    override fun computeScroll() {
        if (helper.continueSettling(true)){
            invalidate()
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return helper.shouldInterceptTouchEvent(ev!!)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        helper.processTouchEvent(event!!)
        return true
    }
}