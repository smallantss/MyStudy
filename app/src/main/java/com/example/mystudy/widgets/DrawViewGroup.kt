package com.example.mystudy.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import com.example.mystudy.R
import kotlinx.android.synthetic.main.layout_move.view.*

class DrawViewGroup @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), View.OnTouchListener {


    var downPointF : PointF? = null
    var movePointF : PointF? = null
    val paint = Paint().apply {
        isAntiAlias = true
        isDither = true
        color = Color.RED
        strokeWidth = 5f
        style = Paint.Style.FILL
    }
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        val focus = ConcreteFocus(v)
        if (v!!.id == R.id.view){
            focus.focusChild()
        }else{
            focus.focusParent()
        }
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                downPointF = focus.down(v,event)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                movePointF = focus.move(v,event)
            }
            MotionEvent.ACTION_UP -> {
                focus.up(v,event)
            }
        }
        return super.onTouchEvent(event)
    }

    init {
        val contentView = LayoutInflater.from(context).inflate(R.layout.layout_move, this, true)
        contentView.view.setOnTouchListener(this)
        setOnTouchListener(this)
//        addView(contentView)
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        canvas?.apply {
            downPointF?.let {
                drawLine(it.x, it.y, movePointF!!.x, movePointF!!.y,paint)
            }
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

}

interface IFocusState {
    fun focusChild()
    fun focusParent()
}

class ConcreteFocus(v: View?) : IFocusState {
    private lateinit var mState: State
    override fun focusChild() {
        mState = ChildState()
    }
    override fun focusParent() {
        mState = ParentState()
    }
    fun down(v: View?, event: MotionEvent) :PointF?{
       return mState.down(v, event)
    }
    fun move(v: View?, event: MotionEvent):PointF?{
        return mState.move(v, event)
    }
    fun up(v: View?, event: MotionEvent) {
        mState.up(v, event)
    }
}

interface State {
    fun down(v: View?, event: MotionEvent):PointF?
    fun move(v: View?, event: MotionEvent):PointF?
    fun up(v: View?, event: MotionEvent)
}

class ChildState : State {
    var downX = 0f
    var moveX = 0f
    val pointF = PointF()

    override fun down(v: View?, event: MotionEvent):PointF? {
        downX = event.x
        pointF.x = event.x
        pointF.y = event.y
        return null
    }

    override fun move(v: View?, event: MotionEvent):PointF? {
        moveX = event.x - downX
        v!!.translationX += moveX
        pointF.x = event.x
        pointF.y = event.y
        return null
    }

    override fun up(v: View?, event: MotionEvent) {

    }
}

class ParentState : State {
    override fun down(v: View?, event: MotionEvent):PointF {
        return PointF()
    }

    override fun move(v: View?, event: MotionEvent):PointF {
        return PointF()
    }

    override fun up(v: View?, event: MotionEvent) {

    }
}