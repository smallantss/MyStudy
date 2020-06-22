package com.example.http.interceptor;

import com.example.http.Request;
import com.example.http.RequestBody;
import com.example.http.Response;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class CallServerIntercept implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        System.out.println("CallServerIntercept  intercept");
        Request request = chain.request();
        URL url = new URL(request.url);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        if (urlConnection instanceof HttpsURLConnection) {
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) urlConnection;
//                    httpsURLConnection.getHostnameVerifier();
//                    httpsURLConnection.getSSLSocketFactory();
        }
        //设置方法和是否能写
        urlConnection.setRequestMethod(request.method.name());
        urlConnection.setDoOutput(request.method.doOutPut());
        urlConnection.connect();
        RequestBody requestBody = request.requestBody;

        //写信息
        if (requestBody!=null){
            requestBody.onWriteBody(urlConnection.getOutputStream());
        }

        int responseCode = urlConnection.getResponseCode();
        if (responseCode == 200) {
            InputStream inputStream = urlConnection.getInputStream();
            Response response = new Response(inputStream);
            return response;
        }
        return null;
    }
}
