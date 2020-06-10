package com.lven.retrofitkotlin

import androidx.multidex.MultiDexApplication
import com.lven.retrofit.config.RestConfig
import com.lven.retrofit.interceptor.CacheInterceptor

class BaseApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        RestConfig.baseUrl("http://httpbin.org")
            .debugUrl("http://httpbin.org")
            .debug(false)
            .register(this)
    }
}