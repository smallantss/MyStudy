package com.example.diy.chapter03

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Path
import android.graphics.RectF
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.widget.LinearLayout
import com.example.mystudy.loge

class RoundLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val path = Path()

    private var bgColor: String = ""

    override fun onDraw(canvas: Canvas?) {
        //先保存背景色，清空，再重新绘制背景色
        if (bgColor.isEmpty()) {
            if (background is ColorDrawable) {
                //十进制数
                val color = (background as ColorDrawable).color
                bgColor = "#".plus(String.format("%08x", color))
                loge("bgColor:$bgColor")
            }
        }
        //清空背景色
        setBackgroundColor(Color.parseColor("#00FFFFFF"))
        super.onDraw(canvas)
    }

    override fun dispatchDraw(canvas: Canvas?) {
        loge("dispatchDraw")
        path.reset()
        path.addRoundRect(RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat()), 50f, 50f, Path.Direction.CW)
        canvas?.save()
        canvas?.clipPath(path)
        if (bgColor.isNotEmpty()) {
            canvas?.drawColor(Color.parseColor(bgColor))
        }
        super.dispatchDraw(canvas)
        canvas?.restore()
    }
}