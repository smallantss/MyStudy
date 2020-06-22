package com.example.mystudy.okhttp;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 实现在获取到请求后30s内读取缓存
 * 处理的是Response
 */
public class CacheResponseInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        response = response.newBuilder()
                .removeHeader("Cache-Control")
                .removeHeader("Pragma")
                //添加过期时间 max-age代表过期时间，秒为单位
                .addHeader("Cache-Control", "max-age=" + 30)
                .build();
        return response;
    }
}
