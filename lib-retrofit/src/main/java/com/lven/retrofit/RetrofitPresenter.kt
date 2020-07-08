package com.lven.retrofit

import android.app.Activity
import com.lven.retrofit.api.RestMethod
import com.lven.retrofit.callback.ICallback
import com.lven.retrofit.callback.RestCallback
import com.lven.retrofit.core.RestClient
import java.io.File

/**
 * 普通网络请求
 */
object RetrofitPresenter : IRetrofit {
    /**
     * 执行方法
     */
    override fun enqueue(
        activity: Activity?,
        method: RestMethod, url: String,
        headers: MutableMap<String, String>?,
        params: MutableMap<String, Any>?,
        callback: ICallback,
        fileDir: File?,
        fileName: String
    ) {
        getRequest(activity, method, url, headers, params, callback)
            .enqueue(
                RestCallback(
                    callback,
                    method == RestMethod.DOWNLOAD,
                    fileDir,
                    fileName
                )
            )
    }

    fun getRequest(
        activity: Activity?,
        method: RestMethod, url: String,
        headers: MutableMap<String, String>? = null,
        params: MutableMap<String, Any>? = null,
        callback: ICallback
    ) = RestClient.RestfulBuilder()
        .method(method)
        .url(url)
        .headers(headers)
        .tag(activity.tag())
        .params(params)
        .build()
        .request(callback)
}

