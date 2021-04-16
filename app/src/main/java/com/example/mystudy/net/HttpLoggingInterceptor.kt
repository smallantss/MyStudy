package com.example.mystudy.net

import com.example.mystudy.loge
import okhttp3.*
import okhttp3.internal.http.HttpHeaders
import okio.Buffer
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.TimeUnit

class HttpLoggingInterceptor : Interceptor {

    private var printLevel: Level = Level.BASIC

    enum class Level {
        //不打印；仅请求和响应首行；仅请求和响应Header；所有数据
        NONE,
        BASIC,
        HEADERS,
        BODY
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (printLevel == Level.NONE) {
            return chain.proceed(request)
        }
        //请求的日志拦截
        logForRequest(request, chain.connection())

        //计算请求时间
        val startNs = System.nanoTime()
        val response = chain.proceed(request)
        //计算结束时间
        val takeMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
        return logForResponse(response, takeMs)
    }

    //打印响应
    private fun logForResponse(response: Response, takeMs: Long): Response {
        val builder = response.newBuilder()
        val clone = builder.build()
        var responseBody = response.body()
        loge("<--${clone.code()} ${clone.message()} ${clone.request().url()} ($takeMs ms)")
        val headers = clone.headers()
        var i = 0
        val count = headers.size()
        while (i < count) {
            loge("\t${headers.name(i)}:${headers.value(i)}")
            i++
        }
        if (HttpHeaders.hasBody(clone)) {
            responseBody ?: return response
            if (isPlaintext(responseBody.contentType())) {
                val bytes = responseBody.byteStream().readBytes()
                val contentType = responseBody.contentType()
                val body = String(bytes, getCharset(contentType)!!)
                loge("\t$body")
                responseBody = ResponseBody.create(responseBody.contentType(), bytes)
                return response.newBuilder().body(responseBody).build()
            } else {
                loge("\tbody: maybe [binary body], omitted!")
            }
        }
        return response
    }

    //打印请求
    private fun logForRequest(request: Request, connection: Connection?) {
        val protocol = if (connection != null) {
            connection.protocol()
        } else {
            Protocol.HTTP_1_1
        }
        val requestBody = request.body()
        //打印请求行
        val requestStartMsg = "--> ${request.method()} ${request.url()} $protocol"
        loge(requestStartMsg)
        if (requestBody != null) {
            if (requestBody.contentType() != null) {
                loge("\tContent-Type:${requestBody.contentType()}")
            }
            if (requestBody.contentLength() != -1L) {
                loge("\tContent-Length:${requestBody.contentLength()}")
            }
        }
        //打印请求头
        val headers = request.headers()
        var i = 0
        val count = headers.size()
        while (i < count) {
            val name = headers.name(i)
            loge("\t$name ${headers.value(i)}")
            i++
        }

        //打印请求正文
        if (isPlaintext(requestBody?.contentType())) {
            bodyToString(request)
        } else {
            loge("\tbody: maybe [binary body], omitted!")
        }
    }

    private fun bodyToString(request: Request) {
        val copy = request.newBuilder().build()
        val body = copy.body() ?: return
        val buffer = Buffer()
        body.writeTo(buffer)
        val charset = getCharset(body.contentType())
        loge("\tbody:${buffer.readString(charset)}")
    }

    private val UTF8 = Charset.forName("UTF-8")

    private fun getCharset(contentType: MediaType?): Charset? {
        var charset = if (contentType != null) contentType.charset(UTF8) else UTF8
        if (charset == null) charset = UTF8
        return charset
    }

    //判断是否是文本
    private fun isPlaintext(mediaType: MediaType?): Boolean {
        mediaType ?: return false
        if (mediaType.type() == "text") {
            return true
        }
        var subType = mediaType.subtype()
        subType = subType.toLowerCase(Locale.getDefault())
        if (subType.contains("x-www-form-urlencoded") || subType.contains("json")
                || subType.contains("xml") || subType.contains("html")) {
            return true
        }
        return false
    }
}