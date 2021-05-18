package com.example.mystudy.audio

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import com.example.mystudy.ui.loge
import java.lang.Exception

object AudioManager {

    private var mAudioTrack: AudioTrack? = null

    fun openAudio() {
        try {
            val bufferSize = AudioTrack.getMinBufferSize(16000, AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT)
            mAudioTrack = AudioTrack(AudioManager.STREAM_MUSIC, 16000,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    bufferSize,
                    AudioTrack.MODE_STREAM)
            loge("init state:${mAudioTrack?.state}")
        } catch (e: Exception) {
            loge("init error")
            e.printStackTrace()
        }
    }

    fun closeAudio() {
        mAudioTrack?.stop()
        mAudioTrack?.release()
    }

    fun play(){
        try {
            mAudioTrack?.play()
        } catch (e: Exception) {
            loge("play error")
            e.printStackTrace()
        }
    }
}