package com.example.mystudy.ui

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mystudy.R
import com.example.mystudy.loge
import com.example.mystudy.utils.BitmapUtils
import com.example.mystudy.utils.Config
import com.example.mystudy.utils.IPUtils
import com.example.socket.MWebSocketServer
import kotlinx.android.synthetic.main.activity_screen.*
import kotlin.properties.Delegates


class ScreenActivity : AppCompatActivity(), ScreenShotHelper.OnScreenShotListener, MWebSocketServer.CallBack {

    private var socketIsStarted = false
    private var webSocketServer: MWebSocketServer by Delegates.notNull()
    private val REQUEST_MEDIA_PROJECTION = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen)

        webSocketServer = MWebSocketServer(Config.ANDROID_SERVER_PORT,this)
        webSocketServer.start();
    }

    override fun onResume() {
        super.onResume()
        tvIp.text = "当前IP:${IPUtils.getIpAddressString()}"
        val rv:RecyclerView
    }

    /**
     * 推送端：1. 开启服务  2. 申请截图权限  3. 传输数据
     */
    @RequiresApi(Build.VERSION_CODES.M)
    fun startQuick(view: View?) {
        if (!socketIsStarted) {
            Toast.makeText(this, "socket 服务启动异常！", Toast.LENGTH_SHORT).show()
        } else {
            tryStartScreenShot()
        }
    }

    /**
     * 播放端：1. 输入IP  2. 接收到数据  3. 展示
     */
    fun join(view: View?) {
        showEditDialog()
    }

    /**
     * 申请截屏权限
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun tryStartScreenShot() {
        val mProjectionManager: MediaProjectionManager = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        if (mProjectionManager != null) {
            startActivityForResult(mProjectionManager.createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_MEDIA_PROJECTION && data != null) {
            if (resultCode == RESULT_OK) {
                // 截屏的回调
                val screenShotHelper = ScreenShotHelper(this, resultCode, data, this)
                screenShotHelper.startScreenShot()
            } else if (resultCode == RESULT_CANCELED) {
                loge(TAG, "用户取消")
            }
        }
    }

    override fun onShotFinish(bitmap: Bitmap) {
        loge(TAG, "bitmap:" + bitmap.getWidth())
        webSocketServer.sendBytes(BitmapUtils.getByteBitmap(bitmap))
    }

    private fun showEditDialog() {
        val editText = EditText(this)
        editText.setText("172.16.1.32")
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Server").setIcon(android.R.drawable.ic_dialog_info).setView(editText)
                .setNegativeButton("Cancel", null)
        builder.setPositiveButton("Ok") { dialog, which ->
            val host: String = editText.getText().toString()
            if (!TextUtils.isEmpty(host)) {
                val intent: Intent = Intent(this@ScreenActivity, PlayActivity::class.java)
                intent.putExtra("host", host)
                startActivity(intent)
            }
        }
        builder.show()
    }

    override fun onServerStatus(isStarted: Boolean) {
        socketIsStarted = isStarted
    }

    override fun onTouchCallback(event: MotionEvent?) {
        window.decorView.dispatchTouchEvent(event)
    }

}