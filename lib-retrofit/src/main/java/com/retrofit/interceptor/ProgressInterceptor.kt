package com.retrofit.interceptor

import com.retrofit.core.RestResponseBody
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 进度条
 */
class ProgressInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // Log.e("TAG", "intercept:${chain.request().url}")
        val response = chain.proceed(chain.request())
        // 这里是进度回调
        response.body?.let {
            return response.newBuilder()
                .body(RestResponseBody(it))
                .build()
        }
        return response
    }
}