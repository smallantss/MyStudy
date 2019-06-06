package com.example.mystudy.widgets

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.Drawable
import java.util.*


class ShowItem(var des: String,var type: Int) {

    var color= getBack()
    //0 正常 1 间隔
//    private var type: Int = 0
    // 0 不管 1 正确 2 错误
    private var isRight: Int = 0

    fun getIsRight(): Int {
        return isRight
    }

    fun setIsRight(isRight: Int) {
        this.isRight = isRight
    }

    private fun getBack(): Drawable {
        val drawable = GradientDrawable()
        drawable.cornerRadius = 8f
        drawable.setColor(Color.rgb(Random().nextInt(255), Random().nextInt(255), Random().nextInt(255)))
        return drawable
    }
}