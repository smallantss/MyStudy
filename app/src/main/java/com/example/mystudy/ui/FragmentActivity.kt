package com.example.mystudy.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import androidx.appcompat.app.AlertDialog
import android.util.Log
import android.view.Choreographer
import android.view.View
import com.example.mystudy.R
import com.example.mystudy.loge
import kotlinx.android.synthetic.main.activity_fragment.*

class FragmentActivity : AppCompatActivity(), Choreographer.FrameCallback, Runnable {
    val tag = "TAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment)
        Log.e(tag, "onCreate")
        testHandler()
        getFps()
    }

    var  fps = 0

    private fun getFps(){
        Choreographer.getInstance().postFrameCallback(this)
        Handler().postDelayed(this,1000)
    }

    private fun testHandler() {
        val handlerThread = HandlerThread("LEAK_CANARY_THREAD_NAME")
        handlerThread.start()
        val backgroundHandler = Handler(handlerThread.looper)
        backgroundHandler.post {
            loge("backgroundHandler:" + Thread.currentThread().name)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.e(tag, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.e(tag, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.e(tag, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.e(tag, "onStop")
    }

    override fun onRestart() {
        super.onRestart()
        Log.e(tag, "onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(tag, "onDestroy")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.e(tag, "onNewIntent")
    }

    private var fm0: BlankFragment2? = null
    private var fm1: BlankFragment? = null
    private var fm2: BlankFragment? = null
    private var fm3: BlankFragment? = null

    fun onClick(v: View) {
//        showDialog()
        hideAll()
        when (v.id) {
            R.id.button0 -> {
//                startActivity(Intent(this, AopActivity::class.java))
                if (fm0 == null) {
                    fm0 = BlankFragment2.newInstance("这是第一个")
                }
                if (fm0!!.isAdded) {
                    supportFragmentManager.beginTransaction().show(fm0!!)
                            .commitNowAllowingStateLoss()
                } else {
                    supportFragmentManager.beginTransaction().add(R.id.frameLayout, fm0!!)
                            .attach(fm0!!)
                            .commitNowAllowingStateLoss()
                }
                fm0!!.setData("改变了0")

            }
            R.id.button1 -> {
                if (fm1 == null) {
                    fm1 = BlankFragment.newInstance("这是第一个")
                }
                if (fm1!!.isAdded) {
                    supportFragmentManager.beginTransaction().show(fm1!!)
                            .commitNowAllowingStateLoss()
                } else {
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout, fm1!!)
                            .show(fm1!!)
                            .commitNowAllowingStateLoss()
                }
                fm1!!.setData("改变了1")
            }
            R.id.button2 -> {
                if (fm2 == null) {
                    fm2 = BlankFragment.newInstance("这是第2个")
                }
                if (fm2!!.isAdded) {
                    supportFragmentManager.beginTransaction().show(fm2!!)
                            .commitNowAllowingStateLoss()
                } else {
                    supportFragmentManager.beginTransaction().add(R.id.frameLayout, fm2!!)
                            .show(fm2!!)
                            .commitNowAllowingStateLoss()
                }
                fm2!!.setData("改变了2")
            }
            R.id.button3 -> {
                if (fm3 == null) {
                    fm3 = BlankFragment.newInstance("这是第3个")
                }
                if (fm3!!.isAdded) {
                    supportFragmentManager.beginTransaction().show(fm3!!)
                            .commitNowAllowingStateLoss()
                } else {
                    supportFragmentManager.beginTransaction().add(R.id.frameLayout, fm3!!)
                            .show(fm3!!)
                            .commitNowAllowingStateLoss()
                }
                fm3!!.setData("改变了3")
            }
        }
    }

    private fun showDialog() {
        AlertDialog.Builder(this)
                .setTitle("标题")
                .setMessage("内容")
                .setPositiveButton("Button") { dialog, which -> dialog.dismiss() }
                .show()
    }

    private fun hideAll() {
        val transaction = supportFragmentManager.beginTransaction()
        if (fm0 != null) {
            transaction.hide(fm0!!)
        }
        if (fm1 != null) {
            transaction.hide(fm1!!)
        }
        if (fm2 != null) {
            transaction.hide(fm2!!)
        }
        if (fm3 != null) {
            transaction.hide(fm3!!)
        }
        transaction.commitNowAllowingStateLoss()
    }

    override fun doFrame(frameTimeNanos: Long) {
        fps++
        Choreographer.getInstance().postFrameCallback(this)
    }

    override fun run() {
        button0.text = fps.toString()
        fps = 0
        Handler().postDelayed(this,1000)
    }


}
