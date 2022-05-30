package com.lven.retrofitkotlin

import androidx.multidex.MultiDexApplication
import com.retrofit.config.RestConfig

class BaseApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        // 1. 初始化
        //val logInterceptor = HttpLoggingInterceptor()
        //logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        RestConfig.baseUrl("http://httpbin.org")
            .debugUrl("http://httpbin.org")
            .debug(BuildConfig.DEBUG)
            //.interceptor(logInterceptor)
            .register(this)
    }
}