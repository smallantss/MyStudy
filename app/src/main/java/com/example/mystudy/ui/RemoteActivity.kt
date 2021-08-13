package com.example.mystudy.ui

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import com.example.mystudy.R
import com.example.mystudy.aidl.ICallback
import com.example.mystudy.aidl.IRemote
import com.example.mystudy.loge
import kotlinx.android.synthetic.main.activity_remote.*

class RemoteActivity : AppCompatActivity(){

    private var mRemote: IRemote? = null

    private val remoteStub = object :ICallback.Stub(){
        override fun callback() {
            loge("server call client")
        }
    }

    private val conn = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            //1.1连接成功时，通过IBinder对象转为服务端对象
            mRemote = IRemote.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mRemote = null
        }
    }

    var data: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remote)
        tvConnect.setOnClickListener {
            //1.连接到远程服务
            val intent = Intent(this, RemoteService::class.java)
            bindService(intent, conn, BIND_AUTO_CREATE)
        }
        tvClientSend.setOnClickListener {
            data++
            //1.2客户端调用服务端的方法
            mRemote?.sendData("client send data:$data")
        }
        tvServerSend.setOnClickListener {
            mRemote?.handleCallback(remoteStub)
        }
    }
}

class RemoteService : Service() {

    var i = 0

    private var client:ICallback?=null

    //2.2binder对象实现了服务端方法
    private val binder = object : IRemote.Stub() {
        override fun sendData(aString: String?) {
            loge("server get data:$aString")
        }

        override fun getData(): String {
            return "server data:${i++}"
        }

        override fun handleCallback(callback: ICallback?) {
            client = callback
            client?.callback()
        }

    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder? {
        //2.1服务端和客户端绑定的时候，返回一个binder对象
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}