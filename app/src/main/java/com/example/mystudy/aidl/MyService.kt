package com.example.mystudy.aidl

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.os.Parcel

const val DESCRIPTOR = "MyService"
class MyService: Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return MyBinder()
    }





}

class MyBinder :Binder(){

    //做数据的处理
    override fun onTransact(code: Int, data: Parcel, reply: Parcel?, flags: Int): Boolean {
        when(code){
            7->{
                data.enforceInterface(DESCRIPTOR)//标识
                val arg0 = data.readInt()
                val arg1 = data.readInt()
                val result = arg0*arg1
                reply?.writeInt(result)
                return true
            }

        }
        return super.onTransact(code, data, reply, flags)
    }
}