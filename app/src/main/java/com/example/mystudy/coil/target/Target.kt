package com.example.mystudy.coil.target

import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.MainThread
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

/**
 * 图像的监听器
 */
interface Target {

    @MainThread
    fun onStart(placeholder: Drawable?) {
    }

    @MainThread
    fun onError(error: Drawable?) {
    }

    @MainThread
    fun onSuccess(success: Drawable?) {
    }
}

interface TransitionTarget : Target {
    val view: View
    val drawable: Drawable?
}

interface ViewTarget<T : View> : Target {

    val view: T

}

interface PoolableViewTarget<T : View> : ViewTarget<T> {

    @MainThread
    fun onClear()
}

open class ImageViewTarget(override val view: ImageView) : PoolableViewTarget<ImageView>, TransitionTarget, DefaultLifecycleObserver {
    override val drawable: Drawable?
        get() = view.drawable

    private var isStarted = false

    override fun onStart(placeholder: Drawable?) = setDrawable(placeholder)

    override fun onError(error: Drawable?) = setDrawable(error)

    override fun onSuccess(success: Drawable?) = setDrawable(success)

    override fun onClear() = setDrawable(null)

    override fun onStart(owner: LifecycleOwner) {
        isStarted = true
        updateAnimation()
    }

    override fun onStop(owner: LifecycleOwner) {
        isStarted = false
        updateAnimation()
    }

    protected open fun setDrawable(drawable: Drawable?) {
        (view.drawable as? Animatable)?.stop()
        view.setImageDrawable(drawable)
        updateAnimation()
    }

    protected open fun updateAnimation() {
        val animatable = (view.drawable as? Animatable) ?: return
        if (isStarted) animatable.start() else animatable.stop()
    }

}