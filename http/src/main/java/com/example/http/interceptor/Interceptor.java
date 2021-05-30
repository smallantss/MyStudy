package com.example.http.interceptor;

import com.example.http.Request;
import com.example.http.Response;

import java.io.IOException;
//拦截器，可以获取请求和响应，并可以对请求做一些操作之后，继续执行
public interface Interceptor {

    Response intercept(Chain chain) throws IOException;

    interface Chain{

        Request request();

        Response proceed(Request request);
    }
}
