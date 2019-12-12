package com.example.http;

public class OkHttpClient {

    Dispatcher dispatcher;
    public OkHttpClient(Builder builder) {
        dispatcher = builder.dispatcher;
    }

    public OkHttpClient(){
        this(new Builder());
    }

    public Call newCall(Request request) {
        return RealCall.newCall(request,this);
    }

    public static class Builder{

        Dispatcher dispatcher;

        public Builder(){
            dispatcher = new Dispatcher();
        }


        public OkHttpClient build(){
            return new OkHttpClient(this);
        }

    }
}
