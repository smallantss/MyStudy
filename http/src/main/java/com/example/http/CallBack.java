package com.example.http;

public interface CallBack {

    void onSuccess(Call call,Response response);

    void onFailure(Call call,Exception e);
}
