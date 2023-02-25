package com.lven.retrofitkotlin

import android.app.Application
import android.content.Context
import com.lven.retrofitkotlin.cookie.NetCookie
import com.retrofit.config.RestConfig
import com.retrofit.core.RestClient
import com.retrofit.interceptor.CacheInterceptor
import okhttp3.CookieJar
import okhttp3.logging.HttpLoggingInterceptor

class BaseApplication : Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        //MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        // 1. 初始化
        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS)
        // http://httpbin.org
        RestConfig.baseUrl("https://www.wanandroid.com")
            .debugUrl("https://www.wanandroid.com")
            .debug(BuildConfig.DEBUG)
            .responseConvert(::convert)
            //.interceptor(ProgressInterceptor())
            .netInterceptor(logInterceptor) // 打印Log日志
            .netInterceptor(CacheInterceptor(3)) // 3秒内再请求，走缓存,get请求生效，post不生效
            .register(this)
        // 配置 Cookie
        RestConfig.cookieJar = NetCookie
    }

    // 自定义响应数据转换：比如后台加密了数据，就可以用这个方法转换
    private fun convert(client: RestClient, data: String): String {
        // 有特殊需求，比如要Base64解密，就可以在这里统一处理
        // client 处理一些特殊的请求，要用client判断
        return data
    }
}