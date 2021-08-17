package com.example.mystudy.ui

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcel
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mystudy.R
import com.example.mystudy.loge
import com.example.mystudy.utils.Config
import com.example.socket.MWebSocketClient
import kotlinx.android.synthetic.main.activity_play.*
import java.net.URI
import java.nio.charset.Charset
import kotlin.concurrent.thread
import kotlin.properties.Delegates


class PlayActivity : AppCompatActivity(), MWebSocketClient.CallBack {

    private var webSocketClient: MWebSocketClient by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        thread {
            val intent: Intent? = intent
            if (intent != null) {
                val host: String = intent.getStringExtra("host")
                try {
                    val url = URI("ws://" + host + ":" + Config.ANDROID_SERVER_PORT)
                    webSocketClient = MWebSocketClient(url, this)
                    webSocketClient.setConnectionLostTimeout(5 * 1000)
                    val flag: Boolean = webSocketClient.connectBlocking()
                    loge("TAG","$flag")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onClientStatus(isConnected: Boolean) {

    }

    var mainHandler: Handler = Handler(Looper.getMainLooper())

    override fun onBitmapReceived(bitmap: Bitmap) {
        mainHandler.post {
            iv.setImageBitmap(bitmap);
        }
    }

    fun tap(view: View) {
        window.decorView.setOnTouchListener { v, event ->
            val parcel = Parcel.obtain()
            event.writeToParcel(parcel,0)
            webSocketClient.send(parcel.marshall())
            return@setOnTouchListener false
        }
    }
}