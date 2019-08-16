package com.example.mystudy

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mystudy.utils.L
import com.example.mystudy.widgets.CustomLayoutManager3
import kotlinx.android.synthetic.main.activity_rv.*
import java.util.*

class RvActivity : AppCompatActivity() {

    private lateinit var datas: ArrayList<String>
    private lateinit var mAdapter:RvAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rv)


        datas = ArrayList<String>()
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
        mAdapter= RvAdapter(datas)
        rv.adapter = mAdapter
    }

    fun onClick(v:View){
        when(v.id){
            R.id.tvAdd->{
                val nextInt = Random().nextInt(datas.size)
                datas.add(nextInt,when(nextInt){
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
                }+nextInt)
                mAdapter.notifyItemInserted(nextInt)
            }
            R.id.tvRemove->{
                if (datas.size==1) return
                val nextInt = Random().nextInt(datas.size)
                datas.removeAt(nextInt)
                mAdapter.notifyItemRemoved(nextInt)
            }
        }

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
