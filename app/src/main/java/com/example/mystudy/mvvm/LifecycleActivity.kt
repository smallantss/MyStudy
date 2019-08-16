package com.example.mystudy.mvvm

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.example.mystudy.R
import com.example.mystudy.utils.NavigationBarUtil
import kotlinx.android.synthetic.main.activity_lifecycle.*
import kotlin.properties.Delegates

class LifecycleActivity : AppCompatActivity(), LifecycleOwner {

    var lifecycleRegistry: LifecycleRegistry by Delegates.notNull()

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lifecycle)
        lifecycleRegistry = LifecycleRegistry(this)
        lifecycleRegistry.addObserver(LocationListener())
        lifecycleRegistry.markState(Lifecycle.State.CREATED)


        val viewModel = ViewModelProviders.of(this).get(PersonPresenter::class.java)
        val personAdapter = PersonAdapter()
        rv.layoutManager = LinearLayoutManager(this)
        rv.addItemDecoration(DividerItemDecoration(this,VERTICAL))
        rv.adapter = personAdapter
        viewModel.allStudents.observe(this, Observer {
            personAdapter.submitList(it)
        })
    }

    override fun onStart() {
        super.onStart()
        lifecycleRegistry.markState(Lifecycle.State.STARTED)
    }


    override fun onDestroy() {
        super.onDestroy()
        lifecycleRegistry.markState(Lifecycle.State.DESTROYED)
    }

    override fun onResume() {
        NavigationBarUtil.fullScreen(window)
        super.onResume()
    }

    fun onClick(v: View) {
    }
}
