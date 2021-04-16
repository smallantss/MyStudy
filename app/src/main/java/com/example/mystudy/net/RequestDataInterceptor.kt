package com.example.mystudy.net

import okhttp3.Interceptor
import okhttp3.RequestBody
import okhttp3.Response
import okio.Buffer
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

class RequestDataInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url().toString()
        val method = request.method()
        if ("POST" == method) {
            val body = request.body()
            val builder = request.url().newBuilder()
            //将原body写入缓存
            val buffer = Buffer()
            body?.writeTo(buffer)
            val reqBuilder = request.newBuilder()
            //未加密的请求参数
            val requestData = URLDecoder.decode(buffer.readString(StandardCharsets.UTF_8), "utf-8")
            //在这里对请求参数进行加密
            val newRequestBody = RequestBody.create(body?.contentType(), requestData)
            val newRequest = reqBuilder.post(newRequestBody).url(builder.build()).build()
            return chain.proceed(newRequest)
        }
        return chain.proceed(request)
    }
}