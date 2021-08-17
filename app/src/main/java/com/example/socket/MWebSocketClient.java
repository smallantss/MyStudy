package com.example.socket;

import android.graphics.Bitmap;

import com.example.mystudy.ExtKt;
import com.example.mystudy.utils.BitmapUtils;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.SocketException;
import java.net.URI;
import java.nio.ByteBuffer;

public class MWebSocketClient extends WebSocketClient {
    private final String TAG = "MWebSocketClient";

    private boolean mIsConnected = false;
    private CallBack mCallBack;

    public MWebSocketClient(URI serverUri, CallBack callBack) {
        super(serverUri);
        this.mCallBack = callBack;
    }

    @Override
    public void onOpen(ServerHandshake handshakeData) {
        ExtKt.loge( "onOpen","");
        updateClientStatus(true);

        try {
            getSocket().setReceiveBufferSize(5 * 1024 * 1024);
        } catch (SocketException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onMessage(String message) {

    }

    @Override
    public void onMessage(ByteBuffer bytes) {
        byte[] buf = new byte[bytes.remaining()];
        bytes.get(buf);
        if (mCallBack != null)
            mCallBack.onBitmapReceived(BitmapUtils.decodeImg(buf));
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        updateClientStatus(false);
    }

    @Override
    public void onError(Exception ex) {
        updateClientStatus(false);
    }

    private void updateClientStatus(boolean isConnected) {

        mIsConnected = isConnected;
        ExtKt.loge(TAG, "mIsConnected:" + mIsConnected);
        // 回调
        if (mCallBack != null)
            mCallBack.onClientStatus(isConnected);
    }

    public boolean isConnected() {
        ExtKt.loge(TAG, "mIsConnected:" + mIsConnected);
        return mIsConnected;
    }

    public interface CallBack {
        void onClientStatus(boolean isConnected);

        void onBitmapReceived(Bitmap bitmap);
    }
}
