package com.example.mystudy.okhttp;

import android.content.Context;

import com.example.mystudy.aop.NetCheckUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 实现在无网情况下只读缓存
 * 处理的是request
 */
public class CacheRequestInterceptor implements Interceptor {

    private Context mContext;

    public CacheRequestInterceptor(Context context) {
        mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!isNetWorkAvailable(mContext)) {
            //只读缓存
            request = request.newBuilder()
                    .cacheControl(new CacheControl.Builder()
                            .onlyIfCached()
                            .build())
                    .build();
        }
        return chain.proceed(request);
    }

    private boolean isNetWorkAvailable(Context context) {
        return NetCheckUtils.getInstance(context).isConnected(context);
    }
}
