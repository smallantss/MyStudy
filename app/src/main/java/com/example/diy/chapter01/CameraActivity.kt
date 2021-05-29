package com.example.diy.chapter01

import android.graphics.Camera
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.ImageView
import android.widget.SeekBar
import com.example.mystudy.R
import kotlinx.android.synthetic.main.activity_camera.*

class CameraActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                iv.progress = progress
                tvProgress.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
        iv.setOnClickListener {
            initOpenAnimation()
        }
    }

    private fun initOpenAnimation() {
        val openAnimation = Rotate3DAnimation(iv, 0f, 180f).apply {
            duration = 3000
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    iv.post {
                        iv.setImageResource(R.mipmap.a)
                    }
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }

            })
        }
        iv.startAnimation(openAnimation)
    }
}

class Rotate3DAnimation(val view: ImageView, val startDegree: Float, val endDegree: Float) : Animation() {

    private var centerX: Int = 0
    private var centerY = 0
    private lateinit var mCamera: Camera

    //要执行的View的宽高，及父控件的宽高，在执行动画前调用
    override fun initialize(width: Int, height: Int, parentWidth: Int, parentHeight: Int) {
        super.initialize(width, height, parentWidth, parentHeight)
        centerX = width / 2
        centerY = height / 2
        mCamera = Camera()
    }

    //实现自定义Animation interpolatedTime动画进度0-1，Transformation对控件应用的变换操作在这个里面
    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        val degree = startDegree + (endDegree - startDegree) * interpolatedTime
        //利用camera将图片绕Y轴旋转degree
        mCamera.save()
        //最大深度为宽度一半
        val z = if (interpolatedTime <= 0.5) {
            centerX * interpolatedTime
        } else {
            view.setImageResource(R.mipmap.b)
            centerX * (1 - interpolatedTime)
        }
        mCamera.translate(0f, 0f, z)
        val matrix = t?.matrix
        mCamera.rotateY(degree)
        mCamera.getMatrix(matrix)
        mCamera.restore()
        //将旋转中心移到中心点位置
        matrix?.preTranslate(-centerX.toFloat(), -centerY.toFloat())
        matrix?.postTranslate(centerX.toFloat(), centerY.toFloat())
        super.applyTransformation(interpolatedTime, t)
    }
}