package com.example.mystudy.ui

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mystudy.R
import com.example.mystudy.ndk.JNI
import com.example.ypermission.PermissionManager
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_ndk.*

class NdkActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ndk)
        val jni = JNI()
        btn1.setOnClickListener {
            //            tv.text = jni.helloFromNdk()
            val p = arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
            PermissionManager
                    .requestPermission(this, p, object : PermissionManager.PermissionsListener() {
                        override fun onPermissionGranted() {
                            Toast.makeText(this@NdkActivity, "permissionGranted", Toast.LENGTH_SHORT).show()
                        }

                        override fun onPermissionDenied(noPermissionsList: MutableList<PermissionManager.NoPermission>) {
                            super.onPermissionDenied(noPermissionsList)
                            Toast.makeText(this@NdkActivity, "permissionDenied", Toast.LENGTH_SHORT).show()
                        }
                    })
        }

        Observable.create<String> {
            it.onNext("111")
            it.onComplete()
        }.subscribe {
            tv.text = it
        }

        val publishSubject = PublishSubject.create<String>()
        publishSubject.subscribe {
            tv.text = it
        }

        var i = 0
        btn2.setOnClickListener {
            i++
            publishSubject.onNext(i.toString())
        }

        btn3.setOnClickListener {
            val list = ArrayList<Observable<String>>()
            for (j in 0 until 5) {
                list.add(Observable.just(j.toString()))
            }
            val observable = Observable.concat(list)
            observable.subscribe {
                Log.e("TAG", it)
            }
        }


    }

    override fun onResume() {
        super.onResume()
    }
}
