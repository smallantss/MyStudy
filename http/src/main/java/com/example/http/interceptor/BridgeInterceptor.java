package com.example.http.interceptor;

import com.example.http.Request;
import com.example.http.RequestBody;
import com.example.http.Response;

public class BridgeInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) {
        System.out.println("BridgeInterceptor  intercept");
        Request request = chain.request();
        //添加请求头
        request.header("Connection","keep-alive");
        RequestBody requestBody = request.requestBody;
        //做一些其他处理
        if (requestBody!=null){
            //头信息
            request.header("Content-Type",requestBody.getContentType());
            request.header("Content-Length",Long.toString(requestBody.getContentLength()));
        }
        return chain.proceed(request);
    }
}
