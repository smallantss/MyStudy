package com.example.mystudy.audio

import android.Manifest
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.Point
import android.media.*
import android.media.AudioTrack.MODE_STREAM
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.SurfaceHolder
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.mystudy.R
import kotlinx.android.synthetic.main.activity_audio.*
import kotlinx.android.synthetic.main.activity_pcm.*
import java.io.*

/**
 * 音视频学习的Activity
 */
const val TAG_AUDIO = "AudioActivity"

fun l(msg: String) {
    Log.e(TAG_AUDIO, msg)
}

/**
 * 利用SurfaceView绘制图片
 */
class AudioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio)
        val point = Point()
        windowManager.defaultDisplay.getSize(point)
        l("w->${point.x},h->${point.y}")
        drawPicOnSurfaceView()
    }

    private fun drawPicOnSurfaceView() {
        surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder?) {
                l("surfaceCreated")
                holder?.let {
                    val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.frog)
                    //使用绘制前先锁定当前的Canvas，获取的是上次的Canvas对象，之前的绘图会被保留，若想擦除，则在绘制之前drawColor进行清屏
                    val canvas = it.lockCanvas()
                    canvas.drawBitmap(bitmap, 0f, 0f, Paint().apply {
                        isAntiAlias = true
                        style = Paint.Style.STROKE
                    })
                    //然后解锁并绘制
                    it.unlockCanvasAndPost(canvas)
                }
            }

            //w,h指该SurfaceView的宽高
            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
                l("surfaceChanged:format-$format,width-$width,height-$height")
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
                l("surfaceDestroyed")
            }

        })
    }
}

/**
 * 使用AudioRecord采集音频PCM并保存到文件
 * 流程：
 *  1.构造AudioRecord。最小录音缓存可通过getMinBufferSize获取,buffer过小将导致创建对象失败
 *  2.初始化Buffer，该buffer等于AudioRecord用于写声音数据的buffer大小
 *  3.开始录音
 *  4.创建数据流，边从AudioRecord读取声音数据到初始化的Buffer边将buffer中数据导入数据流
 *  5.关闭数据流
 *  6.停止录音
 */

class PcmActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var audioRecord: AudioRecord
    //缓冲区大小（可通过API读取）
    private var recordBufSize = 0
    //音频来源（mic）
    private val audioSrc = MediaRecorder.AudioSource.MIC
    //采样率（44100Hz）
    private val rateInHz = 44100
    //通道（单声道，立体声）
    private val channelConfig = AudioFormat.CHANNEL_IN_MONO
    //采样位数（8位16位）
    private val audioFormat = AudioFormat.ENCODING_PCM_16BIT
    private var recording = false
    private lateinit var bufferArray: ByteArray

    private fun initRecord() {
        recordBufSize = AudioRecord.getMinBufferSize(rateInHz, channelConfig, audioFormat)
        audioRecord = AudioRecord(audioSrc, rateInHz, channelConfig, audioFormat, recordBufSize)
    }

    private fun initBuffer() {
        bufferArray = ByteArray(recordBufSize)
    }

    private fun startRecord() {
        recording = true
    }

    private fun readData() {
        Thread {
            val audioFile = File(Environment.getExternalStorageDirectory().absolutePath + "/audio_test.pcm")
            if (!audioFile.exists()) {
                audioFile.createNewFile()
            }
            val fos = FileOutputStream(audioFile)
            audioRecord.startRecording()
            while (recording) {
                val readFlag = audioRecord.read(bufferArray, 0, recordBufSize)
                if (readFlag >= 0) {
                    fos.write(bufferArray)
                }
            }
            fos.close()
        }.start()

    }


    private fun playSound(){
        val minBufferSize = AudioTrack.getMinBufferSize(rateInHz, channelConfig, audioFormat)
        val audioTrack = AudioTrack(audioSrc, rateInHz, channelConfig, audioFormat, minBufferSize, MODE_STREAM)
        val bufferArray = ByteArray(minBufferSize)
        audioTrack.play()
        val audioFile = File(Environment.getExternalStorageDirectory().absolutePath + "/audio_test.pcm")
        val dis = DataInputStream(BufferedInputStream(FileInputStream(audioFile)))
        while (true){
            var i = 0
            while (dis.available()>0&&i<bufferArray.size){
                dis.read(bufferArray)
            }

        }
    }

    private fun stopRecord() {
        recording = false
        audioRecord.apply {
            stop()
            release()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pcm)
        tvStart.setOnClickListener(this)
        tvStop.setOnClickListener(this)
        tvPlay.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvStart -> {
                ActivityCompat.requestPermissions(this,arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO), 0)
            }
            R.id.tvStop -> {
                stopRecord()
            }
            R.id.tvPlay -> {
                playSound()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        initRecord()
        initBuffer()
        startRecord()
        readData()
    }


}

