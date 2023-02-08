package com.lven.retrofitkotlin.cookie

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

/**
 * 网络请求对Cookie的处理
 */
object NetCookie : CookieJar {
    private val loginUrl = "https://www.wanandroid.com/user/login"
    private val loginCookies = arrayListOf<Cookie>()

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        // 实际是从缓存读取
        return loginCookies
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        if (url.toString() == loginUrl) {
            // 实际是缓存到SD卡
            loginCookies.clear()
            loginCookies.addAll(cookies)
        }
    }
}