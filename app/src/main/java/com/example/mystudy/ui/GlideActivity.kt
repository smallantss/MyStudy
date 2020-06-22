package com.example.mystudy.ui

import android.graphics.*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mystudy.R
import kotlinx.android.synthetic.main.activity_glide.*

class GlideActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_glide)
        Glide.with(this).load(R.mipmap.bg_book).apply(RequestOptions().optionalCenterCrop()).into(iv)

        iv2.setImageBitmap(testCenterCrop())

        iv3.setImageBitmap(testRoundCorners(testCenterCrop(),16f))
    }

    val PAINT_FLAGS = Paint.DITHER_FLAG or Paint.FILTER_BITMAP_FLAG
    private val DEFAULT_PAINT = Paint(PAINT_FLAGS)

    private fun testCenterCrop(): Bitmap {
        val height = 300
        val width = 300
        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.frog)
        val scale: Float
        val dx: Float
        val dy: Float
        val m = Matrix()
        if (bitmap.width * height > width * bitmap.height) {
            scale = height.toFloat() / bitmap.height.toFloat()
            dx = (width - bitmap.width * scale) * 0.5f
            dy = 0f
        } else {
            scale = width.toFloat() / bitmap.width.toFloat()
            dx = 0f
            dy = (height - bitmap.height * scale) * 0.5f
        }

        m.setScale(scale, scale)
        m.postTranslate((dx + 0.5f).toInt().toFloat(), (dy + 0.5f).toInt().toFloat())
        val config = if (bitmap.config != null) bitmap.config else Bitmap.Config.ARGB_8888
        val result = Bitmap.createBitmap(width, height, config)
        result.setHasAlpha(bitmap.hasAlpha())

        val canvas = Canvas(result)
        canvas.drawBitmap(bitmap, m, DEFAULT_PAINT)
        canvas.setBitmap(null)
        return result
    }

    private fun testRoundCorners(bitmap:Bitmap,radius: Float): Bitmap {
        //ImageView大小
        val height = 300
        val width = 300
        //原始图片
        val config = if (bitmap.config != null) bitmap.config else Bitmap.Config.ARGB_8888
        //创建空白位图
        val result = Bitmap.createBitmap(width, height, config)
        result.setHasAlpha(true)
        //绘制
        Canvas(result).drawBitmap(bitmap, 0f, 0f, null)
        val shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        val paint = Paint()
        paint.isAntiAlias = true
        paint.shader = shader
        val rect = RectF(0f, 0f, result.width.toFloat(), result.height.toFloat())
        val canvas = Canvas(result)
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        canvas.drawRoundRect(rect, radius, radius, paint)
        canvas.setBitmap(null)
        return result
    }
}
