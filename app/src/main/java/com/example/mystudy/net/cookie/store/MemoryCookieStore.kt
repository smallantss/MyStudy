package com.example.mystudy.net.cookie.store

import okhttp3.Cookie
import okhttp3.HttpUrl

class MemoryCookieStore : CookieStore {

    private val memoryCookies = HashMap<String, MutableList<Cookie>>()

    override fun saveCookie(url: HttpUrl, cookies: MutableList<Cookie>) {
        //内存中的cookie
        val oldCookies = memoryCookies[url.host()]
        val needRemove: MutableList<Cookie?> = ArrayList()
        //新的cookie
        cookies.forEach { new ->
            oldCookies?.forEach { old ->
                //相同的话，删除内存中旧的cookie，添加新的cookie
                if (new.name() == old.name()) {
                    needRemove.add(old)
                }
            }
        }
        oldCookies?.removeAll(needRemove)
        oldCookies?.addAll(cookies)
    }

    override fun saveCookie(url: HttpUrl, cookie: Cookie) {
        val cookies = memoryCookies[url.host()]
        val needRemove: MutableList<Cookie?> = ArrayList()
        cookies?.forEach {
            if (cookie.name() == it.name()) {
                needRemove.add(it)
            }
        }
        cookies?.removeAll(needRemove)
        cookies?.add(cookie)
    }

    override fun loadCookie(url: HttpUrl): MutableList<Cookie> {
        var cookies = memoryCookies[url.host()]
        if (cookies == null) {
            cookies = ArrayList()
            memoryCookies[url.host()] = cookies
        }
        return cookies
    }

    override val allCookie: List<Cookie>
        get() {
            val cookies: MutableList<Cookie> = ArrayList()
            memoryCookies.keys.forEach {
                cookies.addAll(memoryCookies[it]!!)
            }
            return cookies.toMutableList()
        }

    override fun getCookie(url: HttpUrl): List<Cookie>? {
        return memoryCookies[url.host()]
    }

    override fun removeCookie(url: HttpUrl, cookie: Cookie): Boolean {
        return memoryCookies[url.host()]?.remove(cookie) ?: false
    }

    override fun removeCookie(url: HttpUrl): Boolean {
        return memoryCookies.remove(url.host()) != null
    }

    override fun removeAllCookie(): Boolean {
        memoryCookies.clear()
        return true
    }
}