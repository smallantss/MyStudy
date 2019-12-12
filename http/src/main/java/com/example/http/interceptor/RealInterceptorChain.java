package com.example.http.interceptor;

import com.example.http.Request;
import com.example.http.Response;

import java.io.IOException;
import java.util.List;

public class RealInterceptorChain implements Interceptor.Chain {

    private List<Interceptor> interceptors;
    private int index;
    private Request request;
    public RealInterceptorChain(List<Interceptor> interceptors, int index, Request request){
        this.interceptors = interceptors;
        this.index = index;
        this.request = request;
    }

    @Override
    public Request request() {
        return request;
    }

    @Override
    public Response proceed(Request request) {
        RealInterceptorChain next = new RealInterceptorChain(interceptors, index + 1, request);
        Interceptor interceptor = interceptors.get(index);
        Response response = null;
        try {
            response = interceptor.intercept(next);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
