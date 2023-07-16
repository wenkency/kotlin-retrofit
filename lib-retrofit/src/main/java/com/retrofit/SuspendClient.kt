package com.retrofit

/**
 * 协程请求类
 */
object SuspendClient : IRetrofit {

    override fun isSuspendService(): Boolean {
        return true
    }
}

