package com.lven.retrofit.core

import com.google.gson.Gson
import com.lven.retrofit.api.RestService
import com.lven.retrofit.api.RxRestService
import com.lven.retrofit.config.RestConfig
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object RestCreator {
    private const val time = 60L
    private val map = mutableMapOf<String, Retrofit>()
    val gson: Gson by lazy {
        Gson()
    }
    val httpClient: OkHttpClient by lazy {
        var builder = OkHttpClient.Builder()
            .connectTimeout(time, TimeUnit.SECONDS)
            .readTimeout(time, TimeUnit.SECONDS)
            .writeTimeout(time, TimeUnit.SECONDS)
            .hostnameVerifier { _, _ -> true }
        // 添加自定义拦截器
        var interceptors = RestConfig.interceptors
        if (interceptors.isNotEmpty()) {
            for (interceptor in interceptors) {
                builder.addInterceptor(interceptor)
            }
        }
        // 添加网络请求后的自定义拦截器
        var netInterceptors = RestConfig.netInterceptors
        if (netInterceptors.isNotEmpty()) {
            val file: File = RestConfig.context.cacheDir
            if (!file.exists()) {
                file.mkdirs()
            }
            val cache = Cache(file, 100 * 1024 * 1024)
            builder.cache(cache)
            for (interceptor in netInterceptors) {
                builder.addNetworkInterceptor(interceptor)
            }
        }

        builder.build()
    }
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(RestConfig.baseUrl)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(httpClient)
            .build()
    }

    fun getService(): RestService {
        return retrofit.create(RestService::class.java)
    }

    fun getService(url: String): RestService {
        return getRetrofit(url).create(RestService::class.java)
    }


    fun getRxService(): RxRestService {
        return retrofit.create(RxRestService::class.java)
    }

    fun getRxService(url: String): RxRestService {
        return getRetrofit(url).create(RxRestService::class.java)
    }


    private fun getRetrofit(url: String): Retrofit {
        var retrofit = map[url]
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(RestConfig.baseUrl)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(httpClient)
                .build()
            map[url] = retrofit
        }
        return retrofit!!
    }
}