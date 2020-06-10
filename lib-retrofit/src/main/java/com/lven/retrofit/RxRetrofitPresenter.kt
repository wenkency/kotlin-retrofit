package com.lven.retrofit

import android.app.Activity
import com.lven.retrofit.api.RestMethod
import com.lven.retrofit.callback.ICallback
import com.lven.retrofit.callback.RxRestCallback
import com.lven.retrofit.core.RestClient
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

object RxRetrofitPresenter : IRetrofit {
    override fun enqueue(
        activity: Activity?,
        method: RestMethod,
        url: String,
        headers: MutableMap<String, String>?,
        params: MutableMap<String, Any>?,
        callback: ICallback,
        dirName: String,
        fileName: String
    ) {
        if (headers != null) {
            callback.onBefore(headers)
        }
        getRequest(activity, method, url, headers, params, callback)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                RxRestCallback(
                    callback,
                    method == RestMethod.DOWNLOAD,
                    dirName,
                    fileName
                )
            )
    }

    fun getRequest(
        activity: Activity?,
        method: RestMethod,
        url: String,
        headers: MutableMap<String, String>?,
        params: MutableMap<String, Any>?,
        callback: ICallback
    ) = RestClient.RestfulBuilder()
        .method(method)
        .url(url)
        .headers(headers)
        .tag(activity.tag())
        .params(params)
        .build()
        .rxRequest(callback)

}

