package com.example.mystudy.utils

import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.example.mystudy.ui.app
import java.util.*
import kotlin.properties.Delegates

object TtsUtils : UtteranceProgressListener(), LifecycleObserver, TextToSpeech.OnInitListener {

    private var tts: TextToSpeech by Delegates.notNull()

    private var onComplete: (Boolean) -> Unit = {}

    private var playText: String = ""

    private var ready = false

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun create() {
        tts = TextToSpeech(app, this).apply {
            setOnUtteranceProgressListener(this@TtsUtils)
        }
//        setParams()
    }

    //    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        tts.stop()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun release() {
        tts.stop()
        tts.shutdown()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            if (lanAvailable(Locale.CHINA) || lanAvailable(Locale.CHINESE)) {
                ready = true
                if (playText.isNotBlank()) {
                    speak(playText)
                }
            } else {

            }
        } else {
        }
    }

    private fun lanAvailable(locale: Locale): Boolean {
        val r = tts.setLanguage(locale)
        return !(r == TextToSpeech.LANG_MISSING_DATA || r == TextToSpeech.LANG_NOT_SUPPORTED)
    }

    override fun onStart(utteranceId: String?) {

    }

    override fun onDone(utteranceId: String?) {
        onComplete.invoke(true)
    }

    override fun onError(utteranceId: String?) {
        onComplete.invoke(false)
    }

    private fun speak(msg: String) {
        tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    fun setParams(pitch: Float = 1.0f, rate: Float = 0.6f) = tts.apply {
        //音调
        setPitch(pitch)
        //语速
        setSpeechRate(rate)
    }

    fun setAccent(pitch: Float) = tts.apply {
        //音调
        setPitch(pitch)
    }

    fun setSpeed(rate: Float) = tts.apply {
        //语速
        setSpeechRate(rate)
    }

    fun play(msg: String, complete: (Boolean) -> Unit = onComplete) {
        playText = msg
        onComplete = complete
        if (ready) {
            speak(msg)
        }
    }

}