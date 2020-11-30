package com.lven.retrofitkotlin

import com.lven.retrofit.IRetrofit
import com.lven.retrofit.api.RestService
import com.lven.retrofit.core.RestCreator
import okhttp3.OkHttpClient

/**
 * 自定义URL和OkHttpClient
 */
class MultiUrlPresenter : IRetrofit {
    override fun getService(): RestService {
        return RestCreator.getService("http://www.baidu.com/", OkHttpClient());
    }
}