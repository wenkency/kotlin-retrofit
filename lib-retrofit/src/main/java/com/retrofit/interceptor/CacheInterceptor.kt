package com.retrofit.interceptor

import okhttp3.Interceptor
import okhttp3.Response

/**
 * 缓存拦截器
 * 1. 缓存存储策略 public private [no-cache max-age 过期] no-store
 * 2. 缓存过期策略 expires
 * 3. 缓存对比策略 ETag/If-None-Match Last-Modified/If-Modified-Since(过期)
 * 4. @param maxAge 单位是秒
 */
data class CacheInterceptor(val maxAge: Long = 0L) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var cacheControl = request.cacheControl.toString()
        if (maxAge > 0) {
            cacheControl = "private, max-age=$maxAge"
        }
        // 这句话必须调用，不然就中断返回了
        val response = chain.proceed(request)
        return response.newBuilder()
            .removeHeader("Pragma")
            .header("Cache-Control", cacheControl)
            .build()
    }
}