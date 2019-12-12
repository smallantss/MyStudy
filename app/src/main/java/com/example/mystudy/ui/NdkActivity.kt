package com.example.mystudy.ui

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mystudy.R
import com.example.mystudy.ndk.JNI
import com.example.mystudy.rx.permissions.RxPermissions
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
            RxPermissions(this)
                    .request(Manifest.permission.CAMERA)
                    .subscribe {
                        if (it) {
                            Toast.makeText(this, "同意", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(this, "不同意", Toast.LENGTH_LONG).show()
                        }
                    }
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
            for (j in 0 until 5){
                list.add(Observable.just(j.toString()))
            }
            val observable= Observable.concat(list)
            observable.subscribe {
                Log.e("TAG",it)
            }
        }


    }

    override fun onResume() {
        super.onResume()
    }
}
