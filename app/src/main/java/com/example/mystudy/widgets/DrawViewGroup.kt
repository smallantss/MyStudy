package com.example.mystudy.widgets

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import com.example.mystudy.R
import com.example.mystudy.utils.L
import kotlinx.android.synthetic.main.layout_move.view.*

const val TAG = "DrawViewGroup"

class DrawViewGroup @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), View.OnTouchListener {


    private lateinit var mCanvas: Canvas
    private var downPointF: PointF? = null
    private var mPath = Path()
    private val focus = ConcreteFocus()
    private val mPathList = ArrayList<Path>()
    private val paint = Paint().apply {
        isAntiAlias = true
        isDither = true
        color = Color.RED
        strokeWidth = 5f
        style = Paint.Style.STROKE
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (v!!.id == R.id.view) {
            focus.focusChild()
        } else {
            focus.focusParent(mPath, mCanvas)
        }
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                downPointF = focus.down(v, event)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                focus.move(v, event)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                val path = focus.up(v, event)
                path?.let {
                    mPathList.add(it)
                }
            }
        }
        return super.onTouchEvent(event)
    }


    private var bitmap: Bitmap? = null

    init {
        val contentView = LayoutInflater.from(context).inflate(R.layout.layout_move, this, true)
        contentView.view.setOnTouchListener(this)
        val canvas = Canvas()
        contentView.btnRedo.setOnClickListener {
            if (mPathList.isNotEmpty()) {
                //移除最后一条路径
                mPathList.removeAt(mPathList.size - 1)
                //将当前ViewGroup生成bitmap
                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                //画布设置为当前的bitmap，等于重置画布，意味着清空之前的画布
                canvas.setBitmap(bitmap)
                val iterable = mPathList.iterator()
                while (iterable.hasNext()) {
                    //绘制集合里的path
                    canvas.drawPath(iterable.next(), paint)
                }
                //之前的path清除
                mPath.reset()
                invalidate()
            }
        }
        contentView.btnClear.setOnClickListener {
            mPathList.clear()
            mPath.reset()
            bitmap?.recycle()
            bitmap = null
            invalidate()

        }
        setOnTouchListener(this)
    }

    override fun onDraw(canvas: Canvas?) {
        L.e(TAG, "onDraw")
        super.onDraw(canvas)
    }

    override fun dispatchDraw(canvas: Canvas?) {
        L.e(TAG, "dispatchDraw")
        super.dispatchDraw(canvas)
        canvas?.apply {
            mCanvas = this
            drawPath(mPath, paint)
            if (bitmap != null) {
                drawBitmap(bitmap!!, 0f, 0f, paint)
            }
        }
    }
}

interface IFocusState {
    fun focusChild()
    fun focusParent(path: Path, canvas: Canvas)
}

class ConcreteFocus : IFocusState {
    private var mState: State? = null
    override fun focusChild() {
        if (mState == null) {
            mState = ChildState()
        } else if (mState !is ChildState) {
            mState = ChildState()
        }
    }

    override fun focusParent(path: Path, canvas: Canvas) {
        if (mState == null) {
            mState = ParentState(path)
        } else if (mState !is ParentState) {
            mState = ParentState(path)
        }

    }

    fun down(v: View?, event: MotionEvent): PointF? {
        return mState!!.down(v, event)
    }

    fun move(v: View?, event: MotionEvent): Path? {
        return mState!!.move(v, event)
    }

    fun up(v: View?, event: MotionEvent): Path? {
        return mState!!.up(v, event)
    }
}

interface State {
    fun down(v: View?, event: MotionEvent): PointF?
    fun move(v: View?, event: MotionEvent): Path?
    fun up(v: View?, event: MotionEvent): Path?
}

class ChildState : State {
    var downX = 0f
    var moveX = 0f
    val pointF = PointF()

    override fun down(v: View?, event: MotionEvent): PointF? {
        downX = event.x
        pointF.x = event.x
        pointF.y = event.y
        return null
    }

    override fun move(v: View?, event: MotionEvent): Path? {
        moveX = event.x - downX
        v!!.translationX += moveX
        pointF.x = event.x
        pointF.y = event.y
        return null
    }

    override fun up(v: View?, event: MotionEvent): Path? {
        startAnim(v)
        return null
    }

    private fun startAnim(v: View?) {
        val x = v!!.translationX
        ObjectAnimator.ofFloat(v, "translationX", x, 0f).setDuration(200)
                .start()
//        ValueAnimator.ofFloat(x, 0f).apply {
//            duration = 200
//            interpolator = AccelerateDecelerateInterpolator()
//            addUpdateListener { animation -> v.translationX = animation.animatedValue as Float }
//            start()
//        }
    }
}

class ParentState(val p: Path) : State {
    var preX = 0f
    var preY = 0f
    lateinit var path: Path
    override fun down(v: View?, event: MotionEvent): PointF {
        path = Path()
        preX = event.x
        preY = event.y
        p.moveTo(event.x, event.y)
        path.moveTo(event.x, event.y)
        return PointF()
    }

    override fun move(v: View?, event: MotionEvent): Path {
        val endX = (preX + event.x) / 2
        val endY = (preY + event.y) / 2
        path.quadTo(preX, preY, endX, endY)
        p.quadTo(preX, preY, endX, endY)
        preX = event.x
        preY = event.y
        return p
    }

    override fun up(v: View?, event: MotionEvent): Path? {
        return path
    }
}