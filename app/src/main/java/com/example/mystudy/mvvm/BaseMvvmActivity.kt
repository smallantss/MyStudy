package com.example.mystudy.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import java.lang.reflect.ParameterizedType

abstract class BaseMvvmActivity<V:ViewDataBinding, VM:BaseViewModel> : AppCompatActivity() {

    private var viewModel: VM? = null
    private var viewModelId: Int = 0
    private lateinit var binding: V


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //初始化DataBinding
        initViewDataBinding(savedInstanceState)

        initViewModel()

        binding.setVariable(viewModelId,viewModel)
//        lifecycle.addObserver(viewModel)

        initData()
    }

    private fun initViewDataBinding(savedInstanceState: Bundle?) {
        binding = DataBindingUtil.setContentView(this,initContentView(savedInstanceState))
    }

    abstract fun initContentView(savedInstanceState: Bundle?): Int

    private fun initViewModel(){
        //获取ViewModel的ID
        viewModelId = getVariableId()
        //获取自己设置的ViewModel
        viewModel = getViewModel()
        if (viewModel==null) {
            //获取泛型的ViewModel
            var modelClazz: Class<BaseViewModel>
            val type = javaClass.genericSuperclass
            if (type is ParameterizedType){
                modelClazz = type.actualTypeArguments[1] as Class<BaseViewModel>
            }else{
                //自己创建ViewModel
                modelClazz = BaseViewModel::class.java
            }
//            viewModel = createViewModel(this as FragmentActivity,modelClazz) as VM
        }
    }

    //直接利用ViewModelProviders创建ViewModel
//    private fun <T:ViewModel> createViewModel(fragmentActivity: FragmentActivity,clazz: Class<T>): T {
//        return ViewModelProviders.of(fragmentActivity).get(clazz)
//    }

    abstract fun getLayoutId():Int

    abstract fun initData()

    fun getViewModel(): VM? {
        return null
    }

    fun getVariableId() = com.example.mystudy.BR.viewModel


}
