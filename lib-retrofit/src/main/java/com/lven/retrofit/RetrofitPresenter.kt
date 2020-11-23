package com.lven.retrofit

import com.lven.retrofit.api.RestService
import com.lven.retrofit.core.RestCreator

/**
 * 普通网络请求,如果有扩展就继承IRetrofit，参照这个类来写
 * 比如有多个BaseURL，重写getService()方法就可以了
 */
object RetrofitPresenter : IRetrofit {

    /**
     * 可以根据URL获取请求接口
     */
    override fun getService(): RestService {
        return RestCreator.getService()
    }
}

