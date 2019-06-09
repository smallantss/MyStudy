package com.example.mystudy

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.mystudy.utils.L
import com.example.mystudy.widgets.CustomLayoutManager
import com.example.mystudy.widgets.CustomLayoutManager2
import com.example.mystudy.widgets.CustomLayoutManager3
import com.example.mystudy.widgets.CustomLayoutManagerRecyclered
import kotlinx.android.synthetic.main.activity_rv.*
import java.util.*

class RvActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rv)


        val datas = ArrayList<String>()
        for (i in 0..49){
            val nextInt = Random().nextInt(5)
            datas.add(when(nextInt){
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
            }+i)
        }

        rv.layoutManager = CustomLayoutManager3()
        rv.adapter = RvAdapter(datas)
    }

}

class RvAdapter(val datas:List<String>): RecyclerView.Adapter<RvHolder>() {

    var i = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvHolder {
        L.e("RvAdapter","onCreateViewHolder->${i++}")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item, parent,false)
        return RvHolder(view)
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    var j = 1
    override fun onBindViewHolder(holder: RvHolder, position: Int) {
        L.e("RvAdapter","onBindViewHolder->${j++}")
        (holder.itemView as TextView).apply {
            text = datas[position]
            when(position%3){
                0->{setBackgroundColor(Color.parseColor("#0000ff")) }
                1->{setBackgroundColor(Color.parseColor("#00ff00"))}
                2->{setBackgroundColor(Color.parseColor("#ff0000"))}
            }
        }


    }

}

class RvHolder(view:View): RecyclerView.ViewHolder(view) {

}
