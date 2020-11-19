package com.lven.retrofit

import android.app.Activity
import com.lven.retrofit.api.RestMethod
import com.lven.retrofit.api.RestService
import com.lven.retrofit.callback.ICallback
import com.lven.retrofit.callback.RestCallback
import com.lven.retrofit.core.RestClient
import com.lven.retrofit.core.RestCreator
import java.io.File

/**
 * 普通网络请求,如果有扩展就继承IRetrofit，参照这个类来写
 * 比如有多个BaseURL，那就可以定义多个Presenter
 */
object RetrofitPresenter : IRetrofit {
    /**
     * 执行方法
     */
    override fun enqueue(
        activity: Activity?,
        method: RestMethod,
        url: String,
        headers: MutableMap<String, String>?,
        params: MutableMap<String, Any>?,
        callback: ICallback,
        fileDir: File?,
        fileName: String
    ) {
        // 创建请求Click
        val client = RestClient.RestfulBuilder()
            .method(method)
            .url(url)
            .headers(headers)
            .tag(activity.tag())
            .params(params)
            .build()
        // 回调封装
        val restCallback = RestCallback(
            callback,
            method == RestMethod.DOWNLOAD,
            fileDir,
            fileName,
            client
        )
        client.request(callback, getService()).enqueue(restCallback)
    }

    /**
     * 可以根据URL获取请求接口
     */
    private fun getService(url: String? = null): RestService {
        url?.let {
            return RestCreator.getService(it)
        }
        return RestCreator.getService()
    }
}

