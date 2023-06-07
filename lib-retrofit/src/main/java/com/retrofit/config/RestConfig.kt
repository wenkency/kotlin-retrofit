package com.retrofit.config

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.text.TextUtils
import androidx.collection.ArrayMap
import com.retrofit.core.RestClient
import com.retrofit.lifecycle.RestActivityCallbacks
import com.retrofit.utils.RestSSLUtils
import okhttp3.CookieJar
import okhttp3.Interceptor
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

/**
 * 网络请求配置
 */
@SuppressLint("StaticFieldLeak")
object RestConfig {
    lateinit var context: Context
    var isDebug = false




    // 测试的URL
    private var debugUrl = ""

    // 默认URL
    var baseUrl = ""
        get() {
            if (isDebug && !TextUtils.isEmpty(debugUrl)) {
                return debugUrl
            }
            return field
        }

    /**
     * 请求成功前拦截器
     */
    val interceptors = mutableListOf<Interceptor>()

    /**
     * 请求成功后的拦截器
     */
    val netInterceptors = mutableListOf<Interceptor>()

    /**
     * 注册Activity回调，用于Activity销毁时自动取消网络
     *
     */
    private var activityCallbacks: RestActivityCallbacks? = null


    // 默认主机验证
    var hostnameVerifier: HostnameVerifier = HostnameVerifier { _, _ -> true }

    // SSL
    var sslSocketFactory: SSLSocketFactory = RestSSLUtils.initSSLSocketFactory()
    var trustManager: X509TrustManager = RestSSLUtils.initTrustManager()

    // cookie
    var cookieJar: CookieJar? = null


    fun baseUrl(baseUrl: String) = apply {
        this.baseUrl = baseUrl
    }

    fun debugUrl(debugUrl: String) = apply {
        this.debugUrl = debugUrl
    }


    /**
     * 主机校验
     */
    fun hostnameVerifier(hostnameVerifier: HostnameVerifier) = apply {
        this.hostnameVerifier = hostnameVerifier;
    }

    /**
     * 加密工厂
     */
    fun sslSocketFactory(sslSocketFactory: SSLSocketFactory, trustManager: X509TrustManager) =
        apply {
            this.sslSocketFactory = sslSocketFactory
            this.trustManager = trustManager
        }

    fun debug(debug: Boolean) = apply {
        this.isDebug = debug
    }


    /**
     * 应用拦截器
     */
    fun interceptor(interceptor: Interceptor) = apply {
        interceptors.add(interceptor)
    }

    /**
     * 网络拦截器
     */
    fun netInterceptor(interceptor: Interceptor) = apply {
        netInterceptors.add(interceptor)
    }



    /**
     * 注册，目的是监控网络在生命周期销毁时，自动取消
     */
    fun register(application: Application) {
        this.context = application.applicationContext
        if (activityCallbacks == null) {
            activityCallbacks = RestActivityCallbacks()
            application.registerActivityLifecycleCallbacks(activityCallbacks)
        }
    }
}