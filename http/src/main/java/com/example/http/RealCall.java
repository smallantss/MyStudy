package com.example.http;

import com.example.http.interceptor.BridgeInterceptor;
import com.example.http.interceptor.CallServerIntercept;
import com.example.http.interceptor.Interceptor;
import com.example.http.interceptor.RealInterceptorChain;

import java.util.ArrayList;
import java.util.List;

public class RealCall implements Call {

    Request originalRequest;
    OkHttpClient client;

    public RealCall(Request request, OkHttpClient client) {
        this.originalRequest = request;
        this.client = client;
    }

    public static Call newCall(Request request, OkHttpClient client) {
        return new RealCall(request, client);
    }

    @Override
    public void enqueue(CallBack callBack) {
        client.dispatcher.execute(new AsyncCall(callBack));
    }

    @Override
    public Response execute() {
        return null;
    }

    final class AsyncCall extends NamedRunnable {

        CallBack callBack;

        public AsyncCall(CallBack callBack) {
            this.callBack = callBack;
        }

        @Override
        protected void execute() {
            System.out.println("AsyncCall execute ");

            try {
                //正常的写法
/*            Request request = originalRequest;
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

                RequestBody requestBody = request.requestBody;
                if (request!=null){
                    //头信息
                    urlConnection.setRequestProperty("Content-Type",requestBody.getContentType());
                    urlConnection.setRequestProperty("Content-Length",Long.toString(requestBody.getContentLength()));
                }

                urlConnection.connect();

                //写信息
                if (requestBody!=null){
                    requestBody.onWriteBody(urlConnection.getOutputStream());
                }

                int responseCode = urlConnection.getResponseCode();
                if (responseCode == 200) {
                    InputStream inputStream = urlConnection.getInputStream();
                    Response response = new Response(inputStream);
                    callBack.onSuccess(RealCall.this, response);
                }*/


                //仿OkHttp写法
                List<Interceptor> interceptors = new ArrayList<>();
                interceptors.add(new BridgeInterceptor());
                interceptors.add(new CallServerIntercept());
                RealInterceptorChain chain = new RealInterceptorChain(interceptors,0,originalRequest);
                Response response = chain.proceed(originalRequest);
                callBack.onSuccess(RealCall.this, response);
            } catch (Exception e) {
                e.printStackTrace();
                callBack.onFailure(RealCall.this, e);
            }

        }
    }

}
