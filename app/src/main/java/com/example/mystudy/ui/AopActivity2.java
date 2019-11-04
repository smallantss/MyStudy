package com.example.mystudy.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mystudy.R;
import com.example.mystudy.aop.CheckNet;
import com.example.mystudy.aop.CheckNetWithParams;
import com.example.mystudy.aop.NetChange;
import com.example.mystudy.aop.NetCheckUtils2;
import com.example.mystudy.aop.NetType;

public class AopActivity2 extends AppCompatActivity {

    public static final String TAG = "AOP2";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aop);
        NetCheckUtils2.getInstance().register(this);
    }

    @Override
    protected void onDestroy() {
        NetCheckUtils2.getInstance().unregister(this);
        super.onDestroy();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNoParams: {
                noParam();
                break;
            }
            case R.id.btnParams: {
                hasParams(-1);
                break;
            }
            case R.id.annoNoParams: {
                annoNoParams();
                break;
            }
            case R.id.annoParams: {
                annoWithParams();
                break;
            }
        }
    }

    public void noParam() {
        Log.e(TAG, "这是noParam的方法");
    }

    public void hasParams(int a) {
        Log.e(TAG, "这是hasParams的方法->" + a);
        startActivity(new Intent(this, AopActivity.class));
    }

    @CheckNet
    public void annoNoParams() {
        Log.e(TAG, "这是annoNoParams 的方法");
    }

    @CheckNetWithParams(25)
    public void annoWithParams() {
        Log.e(TAG, "这是annoWithParams的方法");
    }

    @NetChange
    public void onNetChange(boolean isConnect) {
        Log.e(TAG, "onNetChange->" + isConnect);
        Toast.makeText(this, "Aop2网络变化了", Toast.LENGTH_SHORT).show();
    }

    @NetChange
    public void onChange(NetType netType) {
        switch (netType) {
            case NET_NO:
                Log.e(TAG, "onNetChange->NET_NO");
                Toast.makeText(this, "变化了-无网络", Toast.LENGTH_SHORT).show();
                break;
            case NET_WIFI:
                Log.e(TAG, "onNetChange->NET_WIFI");
                Toast.makeText(this, "变化了-WiFi", Toast.LENGTH_SHORT).show();
                break;
            case NET_MOBILE:
                Log.e(TAG, "onNetChange->NET_MOBILE");
                Toast.makeText(this, "变化了-mobile", Toast.LENGTH_SHORT).show();
                break;
            case NET_UNKNOWN:
                Log.e(TAG, "onNetChange->NET_UNKNOWN");
                Toast.makeText(this, "变化了-未知", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @NetChange(netType = NetType.NET_MOBILE)
    public void onChange() {
        Log.e(TAG, "onNetChange->无参数");
    }
}
