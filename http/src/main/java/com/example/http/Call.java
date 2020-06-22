package com.example.http;


public interface Call {

    void enqueue(CallBack callBack);

    Response execute();
}
