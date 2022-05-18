package com.retrofit.core

import com.google.gson.Gson
import com.retrofit.api.RestService
import com.retrofit.config.RestConfig
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * 请求接口的构造器
 */
object RestCreator {
    private const val time = 60L
    private val map = mutableMapOf<String, Retrofit>()
    val gson: Gson by lazy {
        Gson()
    }
    val httpClient: OkHttpClient by lazy {
        val builder = OkHttpClient.Builder()
            .connectTimeout(time, TimeUnit.SECONDS)
            .readTimeout(time, TimeUnit.SECONDS)
            .writeTimeout(time, TimeUnit.SECONDS)
            .hostnameVerifier(RestConfig.hostnameVerifier)
        // SSL
        try {
            builder.sslSocketFactory(RestConfig.sslSocketFactory, RestConfig.trustManager)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        // 添加自定义拦截器
        val interceptors = RestConfig.interceptors
        if (interceptors.isNotEmpty()) {
            for (interceptor in interceptors) {
                builder.addInterceptor(interceptor)
            }
        }
        // 添加网络请求后的自定义拦截器
        val netInterceptors = RestConfig.netInterceptors
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

    /**
     * 使用配置的URL创建请求接口
     */
    fun getService(): RestService {
        return getService(RestConfig.baseUrl)
    }

    /**
     * 用户可以根据URL创建请求
     */
    fun getService(url: String, client: OkHttpClient? = null): RestService {
        return getRetrofit(url, client).create(RestService::class.java)
    }

    private fun getRetrofit(url: String, client: OkHttpClient? = null): Retrofit {
        var retrofit = map[url]
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(url)
                .client(client ?: httpClient)
                .build()
            map[url] = retrofit
        }
        return retrofit!!
    }
}