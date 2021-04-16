package com.example.mystudy.net

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.internal.http.HttpHeaders
import java.nio.charset.StandardCharsets

class ResponseInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response = chain.proceed(request)
        val responseBody = response.body()
        val url = request.url().toString()
        if (HttpHeaders.hasBody(response)) {
            responseBody ?: return response
            when (response.code()) {
                503, 504, 500, 404, 403, 400 -> {
                    //异常情况，使用自己的msg返回
                    response = response.newBuilder().code(response.code()).message("连接服务器失败，请稍后再试").build()
                }
                200 -> {
                    val source = responseBody.source()
                    source.request(Long.MAX_VALUE)
                    val buffer = source.buffer
                    val charset = StandardCharsets.UTF_8
                    if (charset != null) {
                        //服务器返回结果，可以处理加解密或token失效等
                        val bodyString = buffer.clone().readString(charset)
//                        val baseBean = Gson().fromJson<*>(bodyString, object : TypeToken<>() {}.type)
//                        when (baseBean.errorCode) {
//                            -1001 -> {
//                                //token失效
//                            }
//                        }
                        val mediaType = responseBody.contentType()
                        val newResponseBody = ResponseBody.create(mediaType, bodyString)
                        response = response.newBuilder().body(newResponseBody).build()
                    }
                }
            }
        }
        return response
    }
}