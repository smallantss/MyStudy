package com.example.http;

import java.util.HashMap;
import java.util.Map;

/**
 * 请求相关的类
 */
public class Request {

    public String url;
    public Method method;
    public Map<String,Object> headers;
    public RequestBody requestBody;

    public Request(Builder builder) {
        url = builder.url;
        method = builder.method;
        headers = builder.headers;
        requestBody = builder.requestBody;
    }

    public void header(String key, String value) {
        headers.put(key,value);
    }

    public static class Builder{
        String url;
        Method method;
        Map<String,Object> headers;
        RequestBody requestBody;

        public Builder(){
            method = Method.GET;
            headers = new HashMap<>();
        }

        public Builder get(){
            method = Method.GET;
            return this;
        }

        public Builder post(RequestBody requestBody){
            method = Method.POST;
            this.requestBody = requestBody;
            return this;
        }

        public Builder url(String url){
            this.url = url;
            return this;
        }

        public Builder header(Map<String,Object> headers){
            this.headers = headers;
            return this;
        }

        public Request build(){
            return new Request(this);
        }
    }
}
