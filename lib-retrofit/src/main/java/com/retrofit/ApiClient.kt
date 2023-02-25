package com.retrofit

/**
 * 普通网络请求,如果有扩展就继承IRetrofit，参照这个类来写
 * 比如有多个BaseURL，重写getService()方法就可以了
 */
object ApiClient : IRetrofit {
    override fun isRxService(): Boolean {
        return false
    }

    override fun isSuspendService(): Boolean {
        return false
    }
}

