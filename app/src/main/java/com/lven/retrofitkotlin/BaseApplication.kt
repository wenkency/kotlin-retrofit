package com.lven.retrofitkotlin

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.lven.retrofitkotlin.cookie.NetCookie
import com.retrofit.config.RestConfig
import okhttp3.logging.HttpLoggingInterceptor

class BaseApplication : Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        // 1. 初始化
        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        // http://httpbin.org
        RestConfig.baseUrl("https://www.wanandroid.com")
            .debugUrl("https://www.wanandroid.com")
            .debug(BuildConfig.DEBUG)
            //.interceptor(ProgressInterceptor())
            //.netInterceptor(logInterceptor) // 打印Log日志
            //.netInterceptor(CacheInterceptor(10)) // 3秒内再请求，走缓存,get请求生效，post不生效
            .register(this)
        // 配置 Cookie
        RestConfig.cookieJar = NetCookie
    }
}