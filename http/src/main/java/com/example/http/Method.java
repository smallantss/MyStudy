package com.example.http;

public enum Method {
    GET("GET"),POST("POST"),PUT("PUT");
    String name;
    Method(String name){
        this.name = name;
    }

    public boolean doOutPut() {
        return name.equals(PUT.name)||name.equals(POST.name);
    }
}
