package com.example.mystudy.net.cookie

import com.example.mystudy.net.cookie.store.CookieStore
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class CookieJarImpl(private val cookieStore: CookieStore) : CookieJar {

    override fun saveFromResponse(url: HttpUrl, cookies: MutableList<Cookie>) {
        cookieStore.saveCookie(url, cookies)
    }

    override fun loadForRequest(url: HttpUrl): MutableList<Cookie> {
        return cookieStore.loadCookie(url)
    }
}