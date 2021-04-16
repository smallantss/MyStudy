package com.example.mystudy.mvvm.livedata

abstract class MyLiveData<T> {

    companion object {
        const val START_VERSION = -1
        val NOT_SET = Any()
    }

    val dataLock = Any()

    private var mData: Any? = null
    private var mVersion: Int = 0
    private var mDispatchingValue:Boolean = false
    private var mDispatchInvalidated:Boolean = false

    constructor(data: T) {
        mData = data
        mVersion = START_VERSION + 1
    }

    constructor() {
        mData = NOT_SET
        mVersion = START_VERSION
    }

    protected fun setValue(value: T) {
        mVersion++
        mData = value
        //分发数据
//        dispatchingValue(null)
    }

    fun dispatchingValue() {
        if (mDispatchingValue){
            mDispatchInvalidated = false
            return
        }
        mDispatchingValue = true
        do {
            mDispatchInvalidated = false
        }while (mDispatchInvalidated)
    }

}
