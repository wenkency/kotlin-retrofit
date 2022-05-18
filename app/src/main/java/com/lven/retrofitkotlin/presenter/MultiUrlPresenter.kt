package com.lven.retrofitkotlin.presenter

import com.retrofit.IRetrofit
import com.retrofit.api.RestService
import com.retrofit.core.RestCreator
import okhttp3.OkHttpClient

/**
 * 自定义URL和OkHttpClient
 */
object MultiUrlPresenter : IRetrofit {
    override fun getService(): RestService {
        return RestCreator.getService("http://httpbin.org", OkHttpClient())
    }
}