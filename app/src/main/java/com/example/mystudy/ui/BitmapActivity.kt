package com.example.mystudy.ui

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.mystudy.R
import com.example.mystudy.utils.L
import kotlinx.android.synthetic.main.activity_bitmap.*

class BitmapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bitmap)
//        iv.post {
//            val bitmap = BitmapFactory.decodeResource(resources,R.mipmap.bg_book)
//            roundedCorners(iv,bitmap)
//        }

        iv2.setOnClickListener {
            (it as CircleView2).changeSrc()
        }

    }
}

//inBitmap 为源bitmap
fun roundedCorners(iv: ImageView, inBitmap: Bitmap) {
    //以ImageView的宽高创建一个空白的Bitmap
    val emptyBitmap = Bitmap.createBitmap(iv.width, iv.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(emptyBitmap)
    val paint = Paint()
    //创建bitmapShader设置填充模式
    val bitmapShader = BitmapShader(inBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
    val matrix = Matrix()
    val scale = iv.width / inBitmap.width.toFloat()
    matrix.setScale(scale, scale)
    bitmapShader.setLocalMatrix(matrix)
    paint.shader = bitmapShader
    val half = iv.width / 2.toFloat()
    canvas.drawCircle(half, half, iv.width / 2.toFloat(), paint)
}

//通过设置BitmapShader绘制圆形
class CircleView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val srcBitmap = BitmapFactory.decodeResource(resources, R.mipmap.bg_book)
    private val paint = Paint()
    //创建bitmapShader设置填充模式
    private val bitmapShader = BitmapShader(srcBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
    private val mMatrix = Matrix()

    override fun onDraw(canvas: Canvas?) {
        val scale = width / srcBitmap.width.toFloat()
        mMatrix.setScale(scale, scale)
        bitmapShader.setLocalMatrix(mMatrix)
        paint.shader = bitmapShader
        val half = width / 2.toFloat()
        canvas?.drawCircle(half, half, width / 2.toFloat(), paint)
    }
}

class CircleView2 @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val mPaint = Paint()
    private val srcBitmap = BitmapFactory.decodeResource(resources, R.mipmap.frog)
    private var src = Rect()
    private var mW = 0
    private var mH = 0
    private lateinit var mC: Canvas
    private lateinit var mEmptyBitmap: Bitmap


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        L.e("onMeasure", "CircleView2")
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        L.e("onSizeChanged", "CircleView2")
        mW = w
        mH = h
        //创建一个空白的Bitmap
        mEmptyBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        //以这个Bitmap为Canvas的底
        mC = Canvas(mEmptyBitmap)
        //源区域 要绘制的bitmap区域,这样代表取bitmap的上半部分
        src.set(0, height / 2, width, height)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        L.e("onFinishInflate", "CircleView2")
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        L.e("onAttachedToWindow", "CircleView2")
    }

    override fun onDraw(canvas: Canvas?) {
        L.e("onDraw", "CircleView2")
        //目标区域 将bitmap绘制在屏幕的什么地方，这里是绘制到左上角，宽高为控件的一半
        val desc = RectF(10f, 10f, width.toFloat(), height.toFloat())
        //在这个以空白Bitmap为底的Canvas上再画一个srcBitmap
        mC.drawBitmap(srcBitmap, src, desc, mPaint)
        mPaint.shader = BitmapShader(srcBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        //之前的操作都是在新的Canvas上，因此这里需要用自己的Canvas再画一次
//        canvas?.drawBitmap(mEmptyBitmap, 0f, 0f, mPaint)
        canvas?.drawCircle(width / 2.toFloat(), height / 2.toFloat(), width / 2.toFloat(), mPaint)
    }

    fun changeSrc() {
        L.e("changeSrc", "aaa")
        val valueAnimator = ValueAnimator.ofFloat(1f, 0f)
        valueAnimator.duration = 200
        valueAnimator.interpolator = AccelerateInterpolator()
        valueAnimator.addUpdateListener {
            // 不断重新计算上下左右位置
            val fraction = it.animatedValue as Float
            L.e("changeSrc", "$fraction")
            src.right = ((fraction * width).toInt())
            // 重绘
            postInvalidate()
        }
        valueAnimator.start()
    }
}

class CircleView3 @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint()
    private lateinit var srcBitmap: Bitmap
    private lateinit var desBitmap: Bitmap

    init {
        setLayerType(LAYER_TYPE_SOFTWARE,null)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        srcBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val c = Canvas(srcBitmap)
        c.drawOval(RectF(0f, 0f, w.toFloat(), h.toFloat()), Paint().apply {
            color = Color.RED
        })

        desBitmap = Bitmap.createBitmap(w / 2, h / 2, Bitmap.Config.ARGB_8888)
        val c2 = Canvas(desBitmap)
        c2.drawRect(RectF(0f, 0f, desBitmap.width.toFloat(), desBitmap.height.toFloat()), Paint().apply {
            color = Color.YELLOW
        })
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            it.drawBitmap(desBitmap, width/2.toFloat(), height/2.toFloat(), paint)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            it.drawBitmap(srcBitmap, 0f, 0f, paint)
            paint.xfermode = null
        }
    }
}

















