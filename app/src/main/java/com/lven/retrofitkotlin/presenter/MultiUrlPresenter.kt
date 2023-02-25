package com.lven.retrofitkotlin.presenter

import com.retrofit.IRetrofit
import com.retrofit.api.ApiService
import com.retrofit.core.RestCreator
import okhttp3.OkHttpClient

/**
 * 自定义URL和OkHttpClient
 */
object MultiUrlPresenter : IRetrofit {

    override fun getService(): ApiService {
        return RestCreator.getService("http://httpbin.org", OkHttpClient())
    }

    override fun isRxService(): Boolean {
        return false
    }

    override fun isSuspendService(): Boolean {
        return false
    }
}