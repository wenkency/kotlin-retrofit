package com.retrofit.config

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.text.TextUtils
import androidx.collection.ArrayMap
import com.retrofit.core.RestClient
import com.retrofit.utils.RestSSLUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
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
    private lateinit var context: Context
    var isDebug = false

    // 请求数据转换，默认是返回自己
    private var requestConvert: (RestClient, String) -> String = { _, it ->
        it
    }

    // 响应数据转换，默认是返回自己
    private var responseConvert: (RestClient, String) -> String = { _, it ->
        it
    }

    // 这个是协程的作用域
    var scope: CoroutineScope = MainScope()

    // 测试的URL
    private var debugUrl = ""

    // 默认URL
    private var baseUrl = ""
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

    /**
     * 公共请求头
     */
    val commHeaders: MutableMap<String, String> = ArrayMap()

    /**
     * 公共请求参数
     */
    val commParams: MutableMap<String, Any> = ArrayMap()

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
     * 请求转换，比如加密要解密，就统一在这里操作
     */
    fun requestConvert(convert: (RestClient, String) -> String) = apply {
        this.requestConvert = convert
    }

    /**
     * 请求转换
     */
    fun requestConvertStr(client: RestClient, data: String): String {
        return requestConvert(client, data)
    }

    /**
     * 响应转换，比如加密要解密，就统一在这里操作
     */
    fun responseConvert(convert: (RestClient, String) -> String) = apply {
        this.responseConvert = convert
    }

    fun responseConvertStr(client: RestClient, data: String): String {
        return responseConvert(client, data)
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
     *  协程作用域：一般用默认就好，请求已用TAG方式取消，不用担心
     */
    fun scope(scope: CoroutineScope) = apply {
        this.scope = scope
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
     * 公共请求头
     */
    fun commHeaders(commHeaders: Map<String, String>) = apply {
        this.commHeaders += commHeaders
    }

    /**
     * 公共请求参数
     */
    fun commParams(commParams: Map<String, Any>) = apply {
        this.commParams += commParams
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