package com.example.mystudy.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.*
import com.example.mystudy.R
import com.example.mystudy.loge
import kotlinx.android.synthetic.main.activity_diy.*
import kotlin.concurrent.thread

class DiyActivity : AppCompatActivity() {

    private val liveData = MutableLiveData(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diy)

        val callback = object : DiffUtil.ItemCallback<Any>() {
            override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
                return true
            }

            override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
                return true
            }
        }
        val adapter = object : ListAdapter<Any, RecyclerView.ViewHolder>(callback) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                return object : RecyclerView.ViewHolder(TextView(parent.context).apply {
                    gravity = Gravity.CENTER
                    setPadding(60)
                    background = ColorDrawable(Color.BLACK)
                    setTextColor(Color.WHITE)
                }) {}
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                (holder.itemView as TextView).text = position.toString()
            }
        }
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
        adapter.submitList(ArrayList<Int>().apply {
            for (i in 0..100) {
                add(i)
            }
        } as List<Any>?)

    }

    private fun testPost() {
//        Handler().postDelayed({
//            loge("w:${root.layoutParams.width}")
//            loge("h:${root.layoutParams.height}")
//            loge("root:${root.width},${root.height}")
//            loge("tvParams:${view.layoutParams.width},${view.layoutParams.height}")
//            loge("tv:${view.width},${view.height}")
//        }, 2000)
    }
}