package com.retrofit.core

import com.google.gson.Gson
import com.retrofit.api.RestService
import com.retrofit.api.RxRestService
import com.retrofit.config.RestConfig
import okhttp3.Cache
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * 请求接口的构造器
 */
object RestCreator {
    private val map = mutableMapOf<String, Retrofit>()

    // 可以修改
    var time = 60L

    // 默认使用的GSON，可以修改
    var gson = Gson()

    val httpClient: OkHttpClient by lazy {
        // 修改最大连接数为69，同一主机同时访问接口数为10
        val dispatcher = Dispatcher()
        dispatcher.maxRequests = 69
        dispatcher.maxRequestsPerHost = 10

        val builder = OkHttpClient.Builder()
            .connectTimeout(time, TimeUnit.SECONDS)
            .readTimeout(time, TimeUnit.SECONDS)
            .writeTimeout(time, TimeUnit.SECONDS)
            .dispatcher(dispatcher)
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
     * 使用配置的URL创建请求接口
     */
    fun getRxService(): RxRestService {
        return getRxService(RestConfig.baseUrl)
    }

    /**
     * 用户可以根据URL创建请求
     */
    fun getRxService(url: String, client: OkHttpClient? = null): RxRestService {
        return getRetrofit(url, client).create(RxRestService::class.java)
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
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(client ?: httpClient)
                .build()
            map[url] = retrofit
        }
        return retrofit!!
    }

    /**
     * 清除所有请求服务
     */
    fun clear() {
        map.clear()
    }
}