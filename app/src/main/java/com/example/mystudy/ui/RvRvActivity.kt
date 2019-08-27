package com.example.mystudy.ui

import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mystudy.R
import com.example.mystudy.data.ChildBean
import com.example.mystudy.data.IType
import com.example.mystudy.data.ParentBean
import kotlinx.android.synthetic.main.activity_rv_rv.*

class RvRvActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rv_rv)

        val data = getData()

        val realData = ArrayList<IType>()
        data.forEach {
            realData.add(it)
            (it as ParentBean).children.forEach {
                realData.add(it)
            }
        }
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = NestedAdapter(realData)
    }

    private fun getData(): ArrayList<IType> {
        return ArrayList<IType>().apply {
            for (i in 1..10) {
                add(ParentBean("这是第$i 个Parent",
                        ArrayList<ChildBean>().apply {
                            for (j in 0 until i)
                                add(ChildBean("这是第$j 个Child"))
                        }))
            }
        }
    }
}

class NestedAdapter(var data: ArrayList<IType>) : RecyclerView.Adapter<NestedHolder>() {


    override fun getItemViewType(position: Int): Int {
        return data[position].getType()
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NestedHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_rv, parent, false)
        view.background = if (viewType == 0) {
            ColorDrawable(ContextCompat.getColor(parent.context, R.color.colorAccent))
        } else {
            ColorDrawable(ContextCompat.getColor(parent.context, R.color.colorPrimary))
        }
        return NestedHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: NestedHolder, position: Int) {
        (holder.itemView as TextView).apply {
            text = if (getItemViewType(position) == 0) {
                setOnClickListener {
                    val childs = (data[position] as ParentBean).children
//                onBindViewHolder(holder, position)
                    data.addAll(position + 1, childs)
                    notifyItemRangeChanged(position + 1, childs.size)
                    notifyItemRangeInserted(position + 1, childs.size)
                }
                (data[position] as ParentBean).name
            } else {
                (data[position] as ChildBean).name
            }

        }
    }

}

class NestedHolder(view: View) : RecyclerView.ViewHolder(view) {

}
