package com.lven.retrofitkotlin

import androidx.multidex.MultiDexApplication
import com.retrofit.config.RestConfig
import okhttp3.logging.HttpLoggingInterceptor

class BaseApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        // 1. 初始化
        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        RestConfig.baseUrl("http://httpbin.org")
            .debugUrl("http://httpbin.org")
            .debug(false)
            //.netInterceptor(logInterceptor) // 打印Log日志
            //.netInterceptor(CacheInterceptor(10)) // 3秒内再请求，走缓存,get请求生效，post不生效
            .register(this)
    }
}