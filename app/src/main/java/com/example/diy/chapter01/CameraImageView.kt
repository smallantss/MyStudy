package com.example.diy.chapter01

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.example.mystudy.R
import com.example.mystudy.ui.loge

class CameraImageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    var progress: Int = 0
        set(value) {
            field = value
            postInvalidate()
        }
    private var bitmap: Bitmap
    private val mPaint = Paint().apply {
        isAntiAlias = true
    }
    private val mCamera = Camera()
    private val mMatrix = Matrix()

    init {
        bitmap = BitmapFactory.decodeResource(resources, R.mipmap.a)
    }

    override fun onDraw(canvas: Canvas?) {
        mCamera.save()
        canvas?.save()
        //保存底部的原图，加个alpha
//        mPaint.alpha = 100
//        canvas?.drawBitmap(bitmap, 0f, 0f, mPaint)
        //旋转画布，应用于canvas
//        mCamera.rotateY(progress.toFloat())
        mCamera.rotateX(progress.toFloat())

        mCamera.getMatrix(mMatrix)

        mMatrix.preTranslate(-width/2f,-height/2f)
        mMatrix.postTranslate(width/2f,height/2f)
        canvas?.setMatrix(mMatrix)

//        mCamera.applyToCanvas(canvas)
        mCamera.restore()
        //绘制原图
        super.onDraw(canvas)
        canvas?.restore()


    }

}