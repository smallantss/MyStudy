package com.example.http.interceptor;

import com.example.http.Request;
import com.example.http.Response;

import java.io.IOException;

public interface Interceptor {

    Response intercept(Chain chain) throws IOException;

    interface Chain{
        Request request();

        Response proceed(Request request);
    }
}
