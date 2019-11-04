package com.example.mystudy.event

import android.view.MotionEvent

class EventStudy {

    //1.

}

//第1个版本：从Activity分发事件到最里面的子View
open class MView1 {

    //传递事件
    open fun passEvent(ev: MotionEvent) {

    }
}

//ViewGroup继承View，里面包含子View
class MViewGroup1(val child: MView1) : MView1() {

    //处理事件直接交给子View处理
    override fun passEvent(ev: MotionEvent) {
        child.passEvent(ev)
    }
}

/**
 * 第2个版本：叠着的View都可以处理事件。同时，用户的一次操作只能被一个View消费处理
 * 因为不能同时一次操作响应多个事件
 * 找到合适处理事件的View，不能是外层的ViewGroup，因为如果他消费了，那么子View就一直不能收到事件了
 * 因此，逻辑应该为，子View不需要事件，再告诉ViewGroup，由ViewGroup判断处理。
 */
open class MView2 {
    var parent: MView2? = null
    //传递进来的
    open fun passIn(ev: MotionEvent) {
        //这里假设不消费，传递出去
        passOut(ev)
    }

    //传给ViewGroup的
    open fun passOut(ev: MotionEvent) {
        parent?.passOut(ev)
    }
}

class MViewGroup2(val child: MView2) : MView2() {
    init {
        child.parent = this
    }

    override fun passIn(ev: MotionEvent) {
        child.passIn(ev)
    }
}

/**
 * 对上面的代码进行优化
 */
open class MView3 {
    //处理事件传递控制逻辑,因为子View没有子View，因此返回值就是onTouch的返回值
    open fun dispatch(ev: MotionEvent): Boolean {
        return onTouch(ev)
    }

    //返回值代表是否消费了事件
    open fun onTouch(ev: MotionEvent): Boolean {
        return false
    }
}

class MViewGroup3(val child: MView3) : MView3() {
    override fun dispatch(ev: MotionEvent): Boolean {
        //首先判断子View要不要消费
        val childHandle = child.dispatch(ev)
//        if (childHandle.not()){
//            //子不消费,看自己消不消费
//            childHandle = onTouch(ev)
//        }
//        return childHandle
        return if (childHandle) {
            //子View消费，则自己不处理,这里需要返回true代表已经被消费了，因为如果传的是ViewGroup的话，childHandle为true被消费，此时假如返回false，还会走自己的onTouch。
            //因此要跟着childHandle来返回
            true
        } else {
            //子View不消费，则看自己消不消费
            onTouch(ev)
        }
    }

    //默认自己不消费，可根据情况修改
    override fun onTouch(ev: MotionEvent): Boolean {
        return false
    }
}

/**
 * 区分触摸事件：DOWN MOVE UP
 * 一次触摸事件，只能被一个View消费。
 * 从DOWN开始，同时需要记住DOWN事件的消费对象，后续事件直接交给他
 */
open class MView4 {
    open fun dispatch(ev: MotionEvent): Boolean {
        return onTouch(ev)
    }

    open fun onTouch(ev: MotionEvent): Boolean {
        return false
    }
}

class MViewGroup4(val child: MView4) : MView4() {
    //子View是否需要处理事件
    private var isChildNeedEvent = false

    override fun dispatch(ev: MotionEvent): Boolean {
        var handle = false
        if (ev.actionMasked == MotionEvent.ACTION_DOWN) {
            clearStatus()
            //子View是否处理了事件
            handle = child.dispatch(ev)
            if (handle) {
                //子View处理事件，代表要接收其他事件
                isChildNeedEvent = true
            } else {
                //子View没有处理，看看自己有没有处理
                handle = onTouch(ev)
            }
        } else {
            //除了DOWN的其他事件，先看看子View要不要处理
            if (isChildNeedEvent) {
                //子View处理的话，继续交给子View处理
                handle = child.dispatch(ev)
            }
            if (handle.not()) {
                //子View没有处理，看看自己有没有处理
                handle = onTouch(ev)
            }
        }
        if (ev.actionMasked == MotionEvent.ACTION_UP) {
            clearStatus()
        }
        return handle
    }

    private fun clearStatus() {
        //重置状态
        isChildNeedEvent = false
    }

    override fun onTouch(ev: MotionEvent): Boolean {
        return false
    }
}

/**
 * 增加外部事件拦截
 * 就是在滑动区域按下点击区域，此时滑动，可以响应滑动
 * 上面的就做不了了，因为响应了点击事件
 * 因为判断用户点击还是滑动不能只判断DOWN事件，还要判断其他事件
 * 比如短时间内UP，就是点击，长时间UP，不MOVE就是长按，短时间MOVE长距离，则是滑动
 * 需要调整dispatch看情况进行拦截，交给自己onTouch处理
 * 父dispatch事件前，增加onIntercept方法，可以覆写自己实现逻辑要不要拦截
 */
open class MView5 {
    open fun dispatch(ev: MotionEvent): Boolean {
        return onTouch(ev)
    }

    open fun onTouch(ev: MotionEvent): Boolean {
        return false
    }
}

open class MViewGroup5(val child: MView5) : MView5() {
    private var isChildNeedEvent = false
    private var isSelfNeedEvent = false

    override fun dispatch(ev: MotionEvent): Boolean {
        var handle = false
        if (ev.actionMasked == MotionEvent.ACTION_DOWN) {
            clearStatus()
            //先判断自己拦不拦截
            if (onIntercept(ev)) {
                //拦截，自己处理
                isSelfNeedEvent = true
                handle = onTouch(ev)
            } else {
                //不拦截,子View处理
                handle = child.dispatch(ev)
                if (handle) {
                    //子View处理了
                    isChildNeedEvent = true
                } else {
                    //子View没有处理,自己处理
                    handle = onTouch(ev)
                    if (handle) {
                        isSelfNeedEvent = true
                    }
                }
            }
        } else {
            //其他的move事件
            if (isSelfNeedEvent) {
                //如果自己处理的话
                handle = onTouch(ev)
            } else if (isChildNeedEvent) {
                //如果子View处理的话，再次判断，为了实现中途拦截
                if (onIntercept(ev)) {
                    isSelfNeedEvent = true
                    handle = onTouch(ev)
                } else {
                    handle = child.dispatch(ev)
                }
            }
        }
        if (ev.actionMasked == MotionEvent.ACTION_UP) {
            clearStatus()
        }
        return handle
    }

    private fun clearStatus() {
        //重置状态
        isChildNeedEvent = false
        isSelfNeedEvent = false
    }

    //是否要拦截,默认不拦截
    open fun onIntercept(ev: MotionEvent): Boolean {
        return false
    }

    override fun onTouch(ev: MotionEvent): Boolean {
        return super.onTouch(ev)
    }
}

/**
 * 增加内部拦截事件
 * 比如子View按下高亮，此时移动父View消费，子View就收不到其他事件了，怎么取消高亮？
 * 在确定要拦截时，把真正的事件转发给自己的onTouch同时，生成一个新的事件给子View，类型为CANCEL，是子View接收到的最后一个事件，收到后可以进行事件的取消
 * 某些情况下，确实需要子View处理，就需要子View明确告知父View不要处理事件即requestDisallowInterceptTouchEvent，同时修改父View状态
 * 父View在准备拦截前先判断这个状态看是否要拦截
 */
interface ViewParent {
    fun requestDisallowInterceptTouchEvent(isDisallowIntercept: Boolean)
}

open class MView6 {
    var parent: ViewParent? = null
    open fun dispatch(ev: MotionEvent): Boolean {
        return onTouch(ev)
    }

    open fun onTouch(ev: MotionEvent): Boolean {
        return false
    }
}

open class MViewGroup6(val child: MView6) : MView6(), ViewParent {
    private var mIsDisallowIntercept = false
    private var isChildNeedEvent = false
    private var isSelfNeedEvent = false
    override fun requestDisallowInterceptTouchEvent(isDisallowIntercept: Boolean) {
        mIsDisallowIntercept = isDisallowIntercept
        parent?.requestDisallowInterceptTouchEvent(mIsDisallowIntercept)
    }

    override fun dispatch(ev: MotionEvent): Boolean {
        var handle = false
        if (ev.actionMasked == MotionEvent.ACTION_DOWN) {
            clearStatus()
            //允许拦截 而且 自己拦截
            if (!mIsDisallowIntercept && onIntercept(ev)) {
                isSelfNeedEvent = true
                handle = onTouch(ev)
            } else {
                //不允许拦截或者不拦截
                handle = child.dispatch(ev)
                if (handle) {
                    //子View处理
                    isChildNeedEvent = true
                } else {
                    //自己处理
                    handle = onTouch(ev)
                    if (handle) {
                        isSelfNeedEvent = true
                    }
                }
            }
        } else {
            //其他事件
            if (isSelfNeedEvent) {
                //自己处理
                handle = onTouch(ev)
            } else if (isChildNeedEvent) {
                //子处理
                if (!mIsDisallowIntercept && onIntercept(ev)) {
                    //自己要拦截
                    isSelfNeedEvent = true
                    //给子View添加CANCEL事件,但是没有继续分发给自己的onTouch,因为已经给子View事件了
                    val cancel = MotionEvent.obtain(ev)
                    cancel.action = MotionEvent.ACTION_CANCEL
                    handle = child.dispatch(cancel)
                    cancel.recycle()
                } else {
                    //子View处理
                    handle = child.dispatch(ev)
                }
            }
        }
        if (ev.actionMasked == MotionEvent.ACTION_UP || ev.actionMasked == MotionEvent.ACTION_CANCEL) {
            clearStatus()
        }
        return handle
    }

    open fun onIntercept(ev: MotionEvent): Boolean {
        return false
    }

    private fun clearStatus() {
        mIsDisallowIntercept = false
        isSelfNeedEvent = false
        isChildNeedEvent = false
    }

    override fun onTouch(ev: MotionEvent): Boolean {
        return super.onTouch(ev)
    }
}

/**
 * 下面是Activity的事件
 * 不会对事件进行拦截
 * 只要有子View没有处理的事件，都交给自己的onTouch
 */
open class MActivity(val child: MViewGroup6) {
    private var isSelfNeedEvent = false
    private var isChildNeedEvent = false

    open fun dispatch(ev: MotionEvent): Boolean {
        var handle = false
        if (ev.actionMasked == MotionEvent.ACTION_DOWN) {
            clearStatus()
            handle = child.dispatch(ev)
            if (handle) {
                isChildNeedEvent = true
            } else {
                handle = onTouch(ev)
                if (handle) {
                    isSelfNeedEvent = true
                }
            }
        } else {
            if (isChildNeedEvent) {
                handle = child.dispatch(ev)
            } else if (isSelfNeedEvent) {
                //这里必须返回true才代表被消费，因此下面的onTouch不会执行2遍
                handle = onTouch(ev)
            }
            if (handle.not()) {
                handle = onTouch(ev)
            }
        }
        if (ev.actionMasked == MotionEvent.ACTION_UP || ev.actionMasked == MotionEvent.ACTION_CANCEL) {
            clearStatus()
        }
        return handle
    }

    private fun clearStatus() {
        isSelfNeedEvent = false
        isChildNeedEvent = false
    }

    open fun onTouch(ev: MotionEvent): Boolean {
        return false
    }
}
