package com.lven.retrofit.config

import android.app.Application
import android.content.Context
import android.text.TextUtils
import androidx.collection.ArrayMap
import okhttp3.Interceptor

/**
 * 网络请求配置
 */
object RestConfig {
    lateinit var context: Context
    internal var isDebug = false

    // 是不是Base64编码--给自己用
    var isBase64 = false
    private var debugUrl = ""
    internal var baseUrl = ""
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


    fun baseUrl(baseUrl: String) = apply {
        this.baseUrl = baseUrl
    }


    fun debugUrl(debugUrl: String) = apply {
        this.debugUrl = debugUrl
    }

    fun debug(debug: Boolean) = apply {
        this.isDebug = debug
    }

    fun base64(isBase64: Boolean) = apply {
        this.isBase64 = isBase64
    }

    fun addInterceptor(interceptor: Interceptor) = apply {
        interceptors.add(interceptor)
    }

    fun addNetInterceptor(interceptor: Interceptor) = apply {
        netInterceptors.add(interceptor)
    }

    fun setCommHeaders(commHeaders: Map<String, String>) = apply {
        this.commHeaders += commHeaders
    }

    fun setCommParams(commParams: Map<String, Any>) = apply {
        this.commParams += commParams
    }

    fun register(application: Application) {
        this.context = application.applicationContext
        if (activityCallbacks == null) {
            activityCallbacks = RestActivityCallbacks()
            application.registerActivityLifecycleCallbacks(activityCallbacks)
        }
    }
}