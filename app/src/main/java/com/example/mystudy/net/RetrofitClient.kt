package com.example.mystudy.net

import com.example.mystudy.net.cookie.CookieJarImpl
import com.example.mystudy.net.cookie.store.MemoryCookieStore
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

class RetrofitClient {

    //超时时间
    private val DEFAULT_TIMEOUT = 20L

    private object SingletonHolder {
        val INSTANCE = RetrofitClient()
    }

    companion object {
        val baseUrl = ""
        var retrofit: Retrofit by Delegates.notNull()
        fun getInstance() = SingletonHolder.INSTANCE

        /**
         * execute(service.login(),{
         *  ...
         * })
         */
        fun <T> execute(observable: Observable<T>, subscriber: Observer<T>) {
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber)
        }
    }

    private constructor() : this(baseUrl, null)

    private constructor(url: String, headers: Map<String, String>?) {
        if (url.isEmpty()) {

        }
        val httpLogInterceptor = HttpLoggingInterceptor()
        val sslParams = HttpsUtil.getSslSocketFactory()
        val okHttpClient = OkHttpClient.Builder()
                .cookieJar(CookieJarImpl(MemoryCookieStore()))
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .addInterceptor(BaseInterceptor(headers))
                .addInterceptor(RequestDataInterceptor())
                .addInterceptor(ResponseInterceptor())
                .addInterceptor(httpLogInterceptor)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                // 这里你可以根据自己的机型设置同时连接的个数和时间，我这里8个，和每个保持时间为10s
                .connectionPool(ConnectionPool(15, 10, TimeUnit.SECONDS))
                .build()
        retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build()
    }

    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }
}