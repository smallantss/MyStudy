package com.example.mystudy.aidl

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.util.Log

class MessengerService: Service() {

    val mHandler = @SuppressLint("HandlerLeak")
    object :Handler(){
        override fun handleMessage(msg: Message?) {
            when(msg?.what){
                110->{
                    Log.e("TAG","接收到客户端的${msg.arg1}")


                    //如果需要回复客户端的话
                    //1.获取到客户端对象
                    val client = msg.replyTo
                    //2.创建一个回复消息Message
                    val replyMessage = Message.obtain()
                    replyMessage.what = 120
                    replyMessage.arg1 = 10
                    //3.发送给客户端
                    client.send(replyMessage)
                }
            }
            super.handleMessage(msg)
        }
    }

    val messenger = Messenger(mHandler)

    override fun onBind(intent: Intent?): IBinder? {
        return messenger.binder
    }


}