package com.retrofit

/**
 * Rxjava方式
 */
object RxClient : IRetrofit {
    override fun isRxService(): Boolean {
        return true
    }
}

