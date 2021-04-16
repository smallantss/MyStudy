package com.example.mystudy.net.cookie.store

import okhttp3.Cookie
import okhttp3.HttpUrl

interface CookieStore {

    //保存url对应的所有cookie
    fun saveCookie(url: HttpUrl, cookies: MutableList<Cookie>)

    //保存url对应的所有cookie
    fun saveCookie(url: HttpUrl, cookie: Cookie)

    //加载url所有的cookie
    fun loadCookie(url: HttpUrl): MutableList<Cookie>

    //获取当前保存的所有cookie
    val allCookie: List<Cookie>

    //获取当前url对应所有cookie
    fun getCookie(url: HttpUrl):List<Cookie>?

    //根据url和cookie移除对应的cookie
    fun removeCookie(url: HttpUrl,cookie: Cookie):Boolean

    //移除url对应的所有cookie
    fun removeCookie(url: HttpUrl):Boolean

    //移除所有的cookie
    fun removeAllCookie():Boolean
}