package com.example.mystudy.event.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.TextView

class MyTextView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {
    private val TAG = MyTextView::class.java.simpleName

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            when(it.action){
                MotionEvent.ACTION_DOWN->{
                    Log.e(TAG,"dispatchTouchEvent:ACTION_DOWN")
                }
                MotionEvent.ACTION_MOVE->{
                    Log.e(TAG,"dispatchTouchEvent:ACTION_MOVE")
                }
                MotionEvent.ACTION_UP->{
                    Log.e(TAG,"dispatchTouchEvent:ACTION_UP")
                }
                else -> {
                    Log.e(TAG,"dispatchTouchEvent:OTHER")
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            when(it.action){
                MotionEvent.ACTION_DOWN->{
                    Log.e(TAG,"onTouchEvent:ACTION_DOWN")
                }
                MotionEvent.ACTION_MOVE->{
                    Log.e(TAG,"onTouchEvent:ACTION_MOVE")
                }
                MotionEvent.ACTION_UP->{
                    Log.e(TAG,"onTouchEvent:ACTION_UP")
                }
                else -> {
                    Log.e(TAG,"onTouchEvent:OTHER")
                }
            }
        }
        return super.onTouchEvent(event)
    }
}