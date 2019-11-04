package com.example.mystudy.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mystudy.R
import com.example.mystudy.aop.*
import kotlin.random.Random

class AopActivity : AppCompatActivity() {

    val TAG = "AopActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aop)
        NetCheckUtils2.getInstance().register(this);
    }

    override fun onDestroy() {
        NetCheckUtils2.getInstance().unregister(this);
        super.onDestroy()
    }

    fun onClick(view:View){
//        startActivity(Intent(this, FragmentActivity::class.java))
//        shakeFun()
//        talk()
        when(view.id){
            R.id.btnNoParams->{
                noParam()
            }
            R.id.btnParams->{
                hasParams(1)
            }
            R.id.annoNoParams->{
                annoNoParams()
            }
            R.id.annoParams->{
                annoWithParams()
            }
        }
    }

    fun shakeFun(){
        val startTimeMillis = System.currentTimeMillis()
        Thread.sleep(Random.nextInt(2000).toLong())
        val endTimeMillis = System.currentTimeMillis()
        val duration = endTimeMillis - startTimeMillis
        Log.e(TAG,"摇一摇花费了$duration")
    }

    fun talk(){
        val startTimeMillis = System.currentTimeMillis()
        Thread.sleep(Random.nextInt(2000).toLong())
        val endTimeMillis = System.currentTimeMillis()
        val duration = endTimeMillis - startTimeMillis
        Log.e(TAG,"聊天花费了$duration")
    }

    fun noParam(){
        startActivity(Intent(this, FragmentActivity::class.java))
        Log.e(TAG,"这是noParam的方法")
    }

    fun hasParams(a:Int){
        Log.e(TAG,"这是hasParams的方法")
    }

    @CheckNet
    fun annoNoParams(){
        Log.e(TAG,"这是annoNoParams 的方法")
    }

    @CheckNetWithParams(25)
    fun annoWithParams(){
        Log.e(TAG,"这是annoWithParams的方法")
    }

    @NetChange
    fun onNetChange(isConnect:Boolean){
        Log.e(TAG, "onNetChange->$isConnect")
        Toast.makeText(this, "Aop网络变化了", Toast.LENGTH_SHORT).show()
    }

    @NetChange(netType = NetType.NET_NO)
    fun onChange(netType: NetType) {
        when (netType) {
            NetType.NET_NO -> {
                Log.e(TAG, "onNetChange->NET_NO")
                Toast.makeText(this, "变化了-无网络", Toast.LENGTH_SHORT).show()
            }
            NetType.NET_WIFI -> {
                Log.e(TAG, "onNetChange->NET_WIFI")
                Toast.makeText(this, "变化了-WiFi", Toast.LENGTH_SHORT).show()
            }
            NetType.NET_MOBILE -> {
                Log.e(TAG, "onNetChange->NET_MOBILE")
                Toast.makeText(this, "变化了-mobile", Toast.LENGTH_SHORT).show()
            }
            NetType.NET_UNKNOWN -> {
                Log.e(TAG, "onNetChange->NET_UNKNOWN")
                Toast.makeText(this, "变化了-未知", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Log.e(TAG, "onNetChange->NET_ALL")
                Toast.makeText(this, "变化了-ALL", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
