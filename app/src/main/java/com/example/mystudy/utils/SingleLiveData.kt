package com.example.mystudy.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.example.mystudy.loge


class SingleLiveData<T> : MutableLiveData<T>() {

    private val myObserverWrapperMap = HashMap<Observer<in T>, MyWrapper<T>>()

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (getV() > -1) {
            if (owner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                //立马调用onChanged
                var wrapper = myObserverWrapperMap[observer]
                if (wrapper == null) {
                    wrapper = MyWrapper(observer)
                    wrapper.preventStickyEvent = true
                    myObserverWrapperMap[observer] = wrapper
                    super.observe(owner, wrapper)
                }
            } else {
                super.observe(owner, observer)
                hook(observer)
            }
        } else {
            super.observe(owner, observer)
        }

    }

    private fun hook(observer: Observer<in T>) {
        //setValue之后，mVersion = 1 ，observer.mLastVersion修改为mVersion
        //LiveData的mObservers属性
        val mObservers = LiveData::class.java.getDeclaredField("mObservers")
        mObservers.isAccessible = true
        //获取当前LiveData的mObservers属性
        val observers = mObservers.get(this)
        //获取SafeIterableMap的class
        val observerClass = observers.javaClass
        //获取SafeIterableMap的get(Observer)方法
        val methodGet = observerClass.getDeclaredMethod("get", Any::class.java)
        methodGet.isAccessible = true
        //执行mObservers.get(Observer)方法
        val objectWrapperEntry = methodGet.invoke(observers, observer)
        var objectWrapper: Any? = null
        if (objectWrapperEntry is Map.Entry<*, *>) {
            objectWrapper = objectWrapperEntry.value
        }
        //获取子类LifecycleBoundObserver
        val wrapperClass = objectWrapper?.javaClass?.superclass
        //获取LifecycleBoundObserver的mLastVersion属性
        val mLastVersionField = wrapperClass?.getDeclaredField("mLastVersion")
        mLastVersionField?.isAccessible = true
        val v = getV()
        loge("version:$v")
        mLastVersionField?.set(objectWrapper, v)
        loge("lastversion:${mLastVersionField?.get(objectWrapper)}")
    }

    private fun getV(): Int {
        val methodGet = LiveData::class.java.getDeclaredMethod("getVersion")
        methodGet.isAccessible = true
        return methodGet.invoke(this) as Int
    }

    override fun onActive() {
        super.onActive()
        loge("onActive")
    }

    override fun onInactive() {
        super.onInactive()
        loge("onInactive")
    }
}

class MyWrapper<T>(val observer: Observer<in T>) : Observer<T> {

    var preventStickyEvent = false

    override fun onChanged(t: T) {
        if (preventStickyEvent) {
            preventStickyEvent = false;
            return;
        }
        observer.onChanged(t);
    }
}

class SingleLiveData2<T> : MutableLiveData<T>() {

    var isAllowNullValue = false

    private val observers: HashMap<Int, Boolean> = HashMap()

    fun observeInActivity(activity: AppCompatActivity, observer: Observer<T>) {
        val owner: LifecycleOwner = activity
        val storeId = System.identityHashCode(observer)
        observe(storeId, owner, observer)
    }

    private fun observe(storeId: Int, owner: LifecycleOwner, observer: Observer<T>) {
        //1.observe的时候，没有存，则为true，存了则不管
        if (observers[storeId] == null) {
            observers[storeId] = true
        }
        super.observe(owner, Observer { t: T? ->
            //为false的时候才可监听，即先setValue，再observe
            if (!observers[storeId]!!) {
                observers[storeId] = true
                if (t != null || isAllowNullValue) {
                    observer.onChanged(t)
                }
            }
        })
    }

    override fun setValue(value: T) {
        if (value!=null || isAllowNullValue){
            //2.设置值的时候都改为false
            observers.entries.forEach {
                it.setValue(false)
            }
            super.setValue(value)
        }
    }

    fun clear(){
        super.setValue(null)
    }
}