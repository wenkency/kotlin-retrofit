package com.lven.retrofit.interceptor

import android.text.TextUtils
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 缓存拦截器
 */
class CacheInterceptor(private val maxAge: Long = 0L) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var cacheControl = request.cacheControl().toString()
        if (maxAge > 0 && TextUtils.isEmpty(cacheControl)) {
            cacheControl = "public, max-age=$maxAge"
        }
        val response = chain.proceed(request)
        return response.newBuilder()
            .header("Cache-Control", cacheControl)
            .removeHeader("Pragma")
            .build()
    }
}