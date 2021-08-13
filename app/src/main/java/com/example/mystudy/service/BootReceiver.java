package com.example.mystudy.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.opengl.opencv.OpencvActivity;

public class BootReceiver extends BroadcastReceiver {

    private static final String TAG = BootReceiver.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "-----wwwwwwwwwww----------"+intent.getAction());

        Intent mIntent = new Intent(context, OpencvActivity.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(mIntent);
    }
}