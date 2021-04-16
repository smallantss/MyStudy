package com.example.mystudy.net

import okhttp3.Interceptor
import okhttp3.Response

class BaseInterceptor(private val headers: Map<String, String>?) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        if (headers != null && headers.isNotEmpty()) {
            val keys = headers.keys
            keys.forEach {
                builder.addHeader(it, headers[it]).build()
            }
        }
        return chain.proceed(builder.build())
    }
}