package com.example.mystudy.ui

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mystudy.R
import com.example.mystudy.databinding.ActivityMvvmRvBinding
import com.example.mystudy.utils.L
import com.example.mystudy.vm.RvControl
import kotlinx.android.synthetic.main.activity_mvvm_rv.*

class MvvmRvActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMvvmRvBinding>(
                this, R.layout.activity_mvvm_rv)

        val vm = ViewModelProviders.of(this).get(RvControl::class.java)
        Log.e("TAG","当前主线程名："+Thread.currentThread().name)
        binding.viewModel = vm
        binding.lifecycleOwner = this
        vm.data.observe(this,androidx.lifecycle.Observer {
            rv.layoutManager = LinearLayoutManager(this)
            val mAdapter = RvAdapter1(vm.data.value!!)
            rv.adapter = mAdapter
        })
    }
}

class RvAdapter1(val datas: List<String>) : RecyclerView.Adapter<RvHolder1>() {

    var i = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvHolder1 {
        L.e("RvAdapter", "onCreateViewHolder->${i++}")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item, parent, false)
        return RvHolder1(view)
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    var j = 1
    override fun onBindViewHolder(holder: RvHolder1, position: Int) {
        L.e("RvAdapter", "onBindViewHolder->${j++}")
        (holder.itemView as TextView).apply {
            text = datas[position]
            when (position % 3) {
                0 -> {
                    setBackgroundColor(Color.parseColor("#0000ff"))
                }
                1 -> {
                    setBackgroundColor(Color.parseColor("#00ff00"))
                }
                2 -> {
                    setBackgroundColor(Color.parseColor("#ff0000"))
                }
            }
        }


    }

}

class RvHolder1(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

}
