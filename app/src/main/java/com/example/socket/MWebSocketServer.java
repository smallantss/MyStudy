package com.example.socket;

import android.os.Parcel;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintSet;

import com.example.mystudy.ExtKt;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class MWebSocketServer extends WebSocketServer {

    private final String TAG = "MWebSocketServer";

    private boolean mIsStarted = false;
    private CallBack mCallBack;

    private List<WebSocket> mWebSocketList;

    public MWebSocketServer(int port, CallBack callBack) {
        super(new InetSocketAddress(port));
        this.mCallBack = callBack;
        setReuseAddr(true);
        setConnectionLostTimeout(5 * 1000);
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake handshake) {
        ExtKt.loge(TAG, "有用户链接");
        if (mWebSocketList == null)
            mWebSocketList = new ArrayList<>();
        mWebSocketList.add(webSocket);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        ExtKt.loge(TAG, "有用户离开");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
//        Parcel parcel = Parcel.obtain();
//        parcel.unmarshall(message.getBytes(),0,message.getBytes().length);
//        parcel.setDataPosition(0);
//        MotionEvent motionEvent = MotionEvent.CREATOR.createFromParcel(parcel);
        ExtKt.loge(TAG, "接收到消息：" + message);

    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(message.array(),0,message.array().length);
        parcel.setDataPosition(0);
        MotionEvent motionEvent = MotionEvent.CREATOR.createFromParcel(parcel);
        ExtKt.loge(TAG, "接收到消息2：" + motionEvent);
        mCallBack.onTouchCallback(motionEvent);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ExtKt.loge(TAG, "发生error:" + ex.toString());
    }

    @Override
    public void onStart() {
        updateServerStatus(true);
    }

    /**
     * 停止服务器
     */
    public void socketStop() {
        try {
            super.stop(100);
            updateServerStatus(false);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送二进制
     *
     * @param bytes
     */
    public void sendBytes(byte[] bytes) {
        if (mWebSocketList == null) return;
        for (WebSocket socket : mWebSocketList)
            socket.send(bytes);
    }

    private void updateServerStatus(boolean isStarted) {
        mIsStarted = isStarted;
        ExtKt.loge(TAG, "mIsStarted:" + mIsStarted);
        // 回调
        if (mCallBack != null)
            mCallBack.onServerStatus(isStarted);
    }

    public boolean isStarted() {
        ExtKt.loge(TAG, "mIsStarted:" + mIsStarted);
        return mIsStarted;
    }

    public interface CallBack {
        void onServerStatus(boolean isStarted);

        void onTouchCallback(MotionEvent event);
    }

}
