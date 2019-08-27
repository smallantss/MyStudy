package com.example.mystudy.widgets

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.util.SparseArray
import android.util.SparseIntArray
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.example.mystudy.R
import com.example.mystudy.utils.NavigationBarUtil
import kotlin.math.max

/**
 * author:xwy
 * date:2018/7/4
 * desc:Builder模式的Dialog
 */
class CommonDialog : Dialog {

    private var mWidth = 0.8f
    private var mIsInBottom = false
    private var isShowOriginWidth = false
    private var mContext: Context

    constructor(context: Context) : this(context, R.style.style_Dialog)

    constructor(context: Context, theme: Int) : super(context, theme) {
        mContext = context
    }

    private fun setWidth(width: Float) {
        mWidth = width
    }

    private fun isInBottom(isInBottom: Boolean) {
        mIsInBottom = isInBottom
    }

    fun setText(viewId: Int, text: String) {
        findViewById<TextView>(viewId).text = text
    }

    fun setVisibility(viewId: Int, visible: Int) {
        findViewById<View>(viewId).visibility = visible
    }

    fun setImage(viewId: Int, resId: Int) {
        findViewById<ImageView>(viewId).setImageResource(resId)
    }

    override fun show() {
        Log.e("TAG", "show->"+window.decorView.width)
        NavigationBarUtil.focusNotAle(window)
        super.show()
        NavigationBarUtil.hideNavigationBar(window)
        NavigationBarUtil.clearFocusNotAle(window)
        if (isShowOriginWidth) return
        Log.e("TAG", window.decorView.layoutParams.toString())
        val defaultDisplay = (mContext as Activity).window.windowManager.defaultDisplay
        val attributes = window.attributes
        val point = Point()
        defaultDisplay.getSize(point)
        attributes.width = (point.x * mWidth).toInt()
        if (mIsInBottom) {
            window.setGravity(Gravity.BOTTOM)
        }
        window.attributes = attributes
    }

    companion object {

        class Builder(c: Context) {
            private var mContext: Context = c
            private var mCancelable = false
            private var mIsInBottom = false
            private lateinit var mTitle: String
            private lateinit var mContent: String
            private var mContentView: View? = null
            private var mContentViewId = -1
            private var textArray = SparseArray<CharSequence>()
            private var imgArray = SparseArray<String>()
            private var visibilityArray = SparseIntArray()
            private var clickArray = SparseArray<OnCommonDialogListener>()
            private var pbArray = SparseArray<OnCommonDialogListener>()
            private var dismissClickArray = ArrayList<Int>()
            private var mWidth = 0.8f
            private var mShowOriginW = false
            private var mOnDismissListener: DialogInterface.OnDismissListener? = null
            private var mStyle = -1
            private var mShowAnim = -1
            private var mHideAnim = -1
            private var pbId = -1
            private var curPb = -1
            private var maxPb = -1
            private var pbListener: OnProgressListener? = null

            init {
                textArray.clear()
                imgArray.clear()
                visibilityArray.clear()
                clickArray.clear()
                pbArray.clear()
                dismissClickArray.clear()
            }

            fun setContentView(contentView: View): Builder {
                mContentViewId = -1
                mContentView = contentView
                return this
            }

            fun setContentView(id: Int): Builder {
                mContentViewId = id
                mContentView = null
                return this
            }

            fun setStyle(style: Int): Builder {
                mStyle = style
                return this
            }

            fun setCancelable(cancel: Boolean): Builder {
                mCancelable = cancel
                return this
            }

            fun isInBottom(isInBottom: Boolean): Builder {
                mIsInBottom = isInBottom
                return this
            }

            fun setText(viewId: Int, text: CharSequence): Builder {
                if (textArray.get(viewId) == null) {
                    textArray.put(viewId, text)
                }
                return this
            }

            fun setVisibility(viewId: Int, visibility: Int): Builder {
                if (visibilityArray.get(viewId) == 0) {
                    visibilityArray.put(viewId, visibility)
                }
                return this
            }

            fun setTitle(title: String): Builder {
                mTitle = title
                return this
            }

            fun setImage(iv: Int, url: String): Builder {
                if (imgArray.get(iv) == null) {
                    imgArray.put(iv, url)
                }
                return this
            }

            fun setOnClick(viewId: Int, onClick: OnCommonDialogListener): Builder {
                if (clickArray.get(viewId) == null) {
                    clickArray.put(viewId, onClick)
                }
                return this
            }

            fun setOnDismissListener(listener: DialogInterface.OnDismissListener): Builder {
                mOnDismissListener = listener
                return this
            }

            fun setOnDismissClick(viewId: Int): Builder {
                dismissClickArray.add(viewId)
                return this
            }

            fun setOnDismissClick(vararg viewId: Int): Builder {
                viewId.forEach {
                    dismissClickArray.add(it)
                }
                return this
            }

            fun setProgress(viewId: Int, pb: Int): Builder {
                pbId = viewId
                curPb = pb
                return this
            }

            fun setMaxPb(max: Int): Builder {
                maxPb = max
                return this
            }

            fun setOnProgressListener(viewId: Int, listener: OnProgressListener): Builder {
                pbId = viewId
                pbListener = listener
                return this
            }

            fun setContent(content: String): Builder {
                mContent = content
                return this
            }

            fun setWidth(width: Float): Builder {
                mWidth = width
                return this
            }

            fun showOriginWidth(): Builder {
                mShowOriginW = true
                return this
            }

            fun setShowAnim(showAnimId: Int): Builder {
                mShowAnim = showAnimId
                return this
            }

            fun setHideAnim(hideAnimId: Int): Builder {
                mHideAnim = hideAnimId
                return this
            }

            fun create(): CommonDialog {
                val newCommonDialog = CommonDialog(mContext)
                if (mStyle != -1) {
                    newCommonDialog.window.setWindowAnimations(mStyle)
                }
                newCommonDialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                newCommonDialog.setCancelable(mCancelable)
                newCommonDialog.setCanceledOnTouchOutside(mCancelable)
                newCommonDialog.setWidth(mWidth)
                newCommonDialog.isShowOriginWidth = mShowOriginW
                newCommonDialog.isInBottom(mIsInBottom)
                if (mOnDismissListener != null) {
                    newCommonDialog.setOnDismissListener(mOnDismissListener)
                }
                val view = if (mContentViewId != -1) {
                    LayoutInflater.from(mContext).inflate(mContentViewId, null)
                } else {
                    mContentView
                }
                view?.let { rootView ->
                    newCommonDialog.setContentView(rootView)
                    if (pbId != -1) {
                        val sb = rootView.findViewById<SeekBar>(pbId)
                        sb.progress = curPb
                        if (maxPb == -1) {
                            maxPb = 100
                        }
                        sb.max = maxPb
                        pbListener?.let{
                            sb.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
                                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                                    it.onProgressChanged(progress,newCommonDialog)
                                }

                                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                                    it.onStartTouch()
                                }

                                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                                    it.onStopTouch()
                                }

                            })
                        }
                    }
                    for (i in 0 until textArray.size()) {
                        val tv = rootView.findViewById<TextView>(textArray.keyAt(i))
                        tv.text = textArray.valueAt(i)
                    }
                    for (i in 0 until clickArray.size()) {
                        val v = rootView.findViewById<View>(clickArray.keyAt(i))
                        v.setOnClickListener {
                            clickArray.valueAt(i).onClick(newCommonDialog, clickArray.keyAt(i))
                        }
                    }
                    for (i in 0 until imgArray.size()) {
                        val v = rootView.findViewById<ImageView>(imgArray.keyAt(i))
//                        GlideUtils.loadImage(mContext, imgArray.valueAt(i), v)
                    }
                    for (i in 0 until visibilityArray.size()) {
                        val v = rootView.findViewById<View>(visibilityArray.keyAt(i))
                        v.visibility = (visibilityArray.valueAt(i))
                    }
                    dismissClickArray.apply {
                        if (isNotEmpty()) {
                            val listener = DismissListener(newCommonDialog)
                            forEach {
                                rootView.findViewById<View>(it).setOnClickListener(listener)
                            }
                        }
                    }
                }
                return newCommonDialog
            }
        }
    }

    class DismissListener(val dialog: CommonDialog) : View.OnClickListener {
        override fun onClick(v: View?) {
            dialog.dismiss()
        }
    }

    interface OnCommonDialogListener {
        fun onClick(dialog: CommonDialog, viewId: Int)
    }

    interface OnProgressListener{

        fun onProgressChanged(progress:Int,dialog:CommonDialog)

        fun onStartTouch(){

        }

        fun onStopTouch(){

        }
    }

}