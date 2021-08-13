package com.example.mystudy.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import com.example.mystudy.R
import com.example.mystudy.loge
import com.example.mystudy.utils.TtsUtils
import kotlinx.android.synthetic.main.activity_audio.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlin.random.Random

lateinit var app: Context

class AudioActivity : AppCompatActivity() {

    private val scope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        app = applicationContext
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio)
        lifecycle.addObserver(TtsUtils)
        sbSpeed.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                //500-5.0 100-1.0
                val speed = progress / 100f
                tvSpeed.text = "语速:${speed}"
                TtsUtils.setSpeed(speed)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
        sbAccent.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val speed = progress / 100f
                tvAccent.text = "音调:$speed"
                TtsUtils.setAccent(speed)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
        button4.setOnClickListener {
            //开始
            flag = true
            start2()
        }
        button5.setOnClickListener {
            //停止
            flag = false
        }
    }

    private var flag = true
    private var playJob: Job? = null
    private fun start() {
        playJob?.cancel()
        playJob = scope.launch {
            flow {
                while (flag) {
                    delay(2000)
                    emit(Random.nextInt(1, 50).toString().plus("床呼叫"))
                }
            }.collect {
                loge(it)
                TtsUtils.play(it, {

                })
            }
        }
    }

    private fun start2() {
        val text = et.text.toString()
//        val text = Random.nextInt(1, 50).toString().plus("床呼叫")
        TtsUtils.play(text) {
            scope.launch {
                delay(1000)
                if (flag) {
                    start2()
                }
            }
        }
    }
}