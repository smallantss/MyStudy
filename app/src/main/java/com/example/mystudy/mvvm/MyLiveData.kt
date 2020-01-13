package com.example.mystudy.mvvm

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

class MyLiveData {

    val num1 = MutableLiveData<Int>()
    val num2 = MutableLiveData<Int>()
    val result =MediatorLiveData<Int>()

    init {
        result.addSource(num1) {
            Log.e("TAG", "num1: $it")
        }
        result.addSource(num2) {
            Log.e("TAG", "num2: $it")
        }
    }

    fun setNum1Data(num:Int){
        num1.value = num
    }

    fun setNum2Data(num:Int){
        num2.value = num
    }

    fun getResult():Int{
        return result.value!!
    }
}