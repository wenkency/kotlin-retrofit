package com.retrofit.interceptor

import android.text.TextUtils
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 缓存拦截器
 */

data class CacheInterceptor(val maxAge: Long = 0L) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var cacheControl = request.cacheControl.toString()
        if (maxAge > 0 && TextUtils.isEmpty(cacheControl)) {
            cacheControl = "public, max-age=$maxAge"
        }
        // 这句话必须调用，不然就中断返回了
        val response = chain.proceed(request)
        return response.newBuilder()
            .removeHeader("Pragma")
            .header("Cache-Control", cacheControl)
            .build()
    }
}