package com.example.mystudy.ui

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.mystudy.R
import kotlinx.android.synthetic.main.activity_vg.*
import java.util.*

class VgActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vg)

        for (i in 0..49){
            val nextInt = Random().nextInt(5)
            fl.addView(TextView(this).apply {
//                layoutParams = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT).apply {
//                    leftMargin = 60
//                    rightMargin = 60
//                    topMargin = 40
//                    bottomMargin = 40
//                }
                setPadding(60,40,60,40)
                when(nextInt%3){
                    0->{setBackgroundColor(Color.parseColor("#0000ff")) }
                    1->{setBackgroundColor(Color.parseColor("#00ff00"))}
                    2->{setBackgroundColor(Color.parseColor("#ff0000"))}
                }
                setTextColor(Color.parseColor("#ffffff"))
                text = when(nextInt){
                    0->{
                        "是"
                    }
                    1->{
                        "明天"
                    }
                    2->{
                        "辛啊啊"
                    }
                    3->{
                        "长得真帅"
                    }
                    4->{
                        "当真是这样？"
                    }
                    else -> ""
                }+i
            })
        }

    }
}

