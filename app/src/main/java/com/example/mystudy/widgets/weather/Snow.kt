package com.example.mystudy.widgets.weather

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View

class Snow @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val moonPaint = Paint().apply {
        isAntiAlias = true
        isDither = true
        // 光晕的颜色
        color = Color.parseColor("#e6e8db")
        maskFilter = BlurMaskFilter(50f, BlurMaskFilter.Blur.SOLID)
    }
    private val moonRadius = 200F

    private val snowFlakes = ArrayList<SnowFlake2>().apply {
        for (i in 0 until 100) {
            add(SnowFlake2())
        }
    }

    init {
        //关闭硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    private var posY = 0f

    override fun onDraw(canvas: Canvas?) {
        Log.e("TAG",Thread.currentThread().name)
        val centerX = (width / 2).toFloat()
        val centerY = (height / 2).toFloat()
        canvas?.let {
            //画夜晚
            it.drawColor(Color.BLACK)

            //画月亮
            it.drawCircle(centerX, centerY, moonRadius, moonPaint)

            //画同心圆，这个是月亮的实体
            val moonDis = 20
            moonPaint.shader = LinearGradient(centerX - moonRadius, centerY + moonRadius, centerX + moonRadius, centerY - moonRadius,
                    Color.parseColor("#e0e2e5"), Color.parseColor("#758595"), Shader.TileMode.CLAMP)
            it.drawCircle(centerX, centerY, moonRadius - moonDis, moonPaint)

            it.save()
            it.rotate(10f, centerX, centerY)
            //画手臂
            val rectf = RectF().apply {
                left = centerX - 120
                top = centerY - 120
                right = centerX + 120
                bottom = centerY + 120
            }
            it.drawArc(rectf, 45f, 90f, false, Paint().apply {
                isAntiAlias = true
                isDither = true
                strokeWidth = 10f
                style = Paint.Style.STROKE
                alpha = 120
                color = Color.BLACK
            })

            //雪人身体的半径
            val snowBodyRadius = 50f
            it.drawCircle(centerX, centerY + moonRadius - snowBodyRadius - moonDis + 5, snowBodyRadius, Paint().apply {
                isAntiAlias = true
                isDither = true
                color = Color.parseColor("#e6e8db")
            })

            //雪人头部的半径
            val snowHeadRadius = 25f
            it.drawCircle(centerX, centerY + moonRadius - snowBodyRadius * 2 - moonDis - snowHeadRadius + 10, snowHeadRadius, Paint().apply {
                isAntiAlias = true
                isDither = true
                color = Color.parseColor("#e6e8db")
            })
            it.restore()

            snowFlakes.forEachIndexed { index, flake ->
                //                flake.posX = centerX - moonRadius + Math.random().toFloat() * moonRadius * 2
//                flake.radius = Math.random().toFloat() * 20
                flake.centerX = centerX
                flake.centerY = centerY
                flake.draw(it)
            }

            postDelayed({
                invalidate()
            }, 100)
        }
    }

}

class SnowFlake {

    //起始位置
    var posX = 0f
    var posY = Math.random().toFloat() * 200
    //速度
    private val speedX = 5f
    private val speedY = 10f
    //半径
    var radius = 5f

    private val paint = Paint().apply {
        isAntiAlias = true
        isDither = true
        style = Paint.Style.FILL
        color = Color.parseColor("#FFFFFF")
        alpha = 100
    }

    fun draw(canvas: Canvas) {
        posY += Math.random().toFloat() * 20
        canvas.drawCircle(posX, posY, radius, paint)
    }
}

class SnowFlake2(
        var radius: Float? = null,
        val speed: Float? = null,
        val angle: Float? = null
//        val moveScopeX: Float? = null,
//        val moveScopeY: Float? = null
) {
    private val TAG = "SnowFlake2"
    private val random = java.util.Random()
     var centerX = 0f
    set(value) {
        field = value
        presentX = random.nextInt(centerX?.toInt() ?: 0).toFloat()
    }
     var centerY = 0f
    set(value) {
        field = value
        presentY = random.nextInt(centerY?.toInt() ?: 0).toFloat()
    }
    private var presentX = 0f
    private var presentY = 0f
    private var presentSpeed = getSpeed()
    private var presentAngle = getAngle()
    private var presentRadius = getRadius()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#e6e8db")
        alpha = 100
    }


    // 绘制雪花
    fun draw(canvas: Canvas) {
        moveX()
        moveY()
        if (centerX != null && centerY != null) {
            if (presentX > centerX || presentY > centerY || presentX < 0 || presentY < 0) {
                reset()
            }
        }
        canvas.drawCircle(presentX, presentY, presentRadius, paint)
    }

    // 移动雪花（x轴方向）
    fun moveX() {
        presentX += getSpeedX()
    }

    // 移动雪花（Y轴方向）
    fun moveY() {
        presentY += getSpeedY()
    }

    fun getSpeed(): Float {
        var result: Float
        speed.let {
            result = it ?: (random.nextFloat() + 1)
        }
        Log.e(TAG, "speed: $result")
        return result
    }

    fun getRadius(): Float {
        var size: Float
        radius.let {
            size = it ?: random.nextInt(15).toFloat()
        }
        return size
    }

    fun getAngle(): Float {
        angle.let {
            if (it != null) {
                if (it > 30) {
                    return 30f
                }
                if (it < 0) {
                    return 0f
                }
                return it
            } else {
                return random.nextInt(30).toFloat()
            }
        }
    }

    fun getSpeedX(): Float {
        return (presentSpeed * Math.sin(presentAngle.toDouble())).toFloat()
    }

    fun getSpeedY(): Float {
        return (presentSpeed * Math.cos(presentAngle.toDouble())).toFloat()
    }

    fun reset() {
        presentSpeed = getSpeed()
        presentAngle = getAngle()
        presentRadius = getRadius()
        presentX = random.nextInt(centerX?.toInt() ?: 0).toFloat()
        presentY = 0f
    }

    data class Builder(
            var mRadius: Float? = null,
            var mSpeed: Float? = null,
            var mAngle: Float? = null,
            var moveScopeX: Float? = null,
            var moveScopeY: Float? = null
    ) {
        fun radius(radius: Float) = apply { this.mRadius = radius }
        fun speed(speed: Float) = apply { this.mSpeed = speed }
        fun angle(angle: Float) = apply { this.mAngle = angle }
        fun scopeX(scope: Float) = apply { this.moveScopeX = scope }
        fun scopeY(scope: Float) = apply { this.moveScopeY = scope }
        fun build() = SnowFlake2(mRadius, mSpeed, mAngle)
    }
}