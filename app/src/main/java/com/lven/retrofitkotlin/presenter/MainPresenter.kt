package com.lven.retrofitkotlin.presenter

import android.util.ArrayMap
import com.lven.retrofitkotlin.bean.Bean
import com.retrofit.ApiClient
import com.retrofit.callback.ICallback
import com.retrofit.callback.IObjectCallback
import com.retrofit.callback.ObjectCallback

object MainPresenter {
    /**
     * Object回调
     */
    fun postObj(
        activity: Any?, callback: IObjectCallback,
        clazz: Class<*>, requestCode: Int = 1
    ) {
        val map = ArrayMap<String, Any>()
        map["id"] = 100
        MultiUrlPresenter.post(
            activity,
            "post",
            map,
            ObjectCallback(callback, clazz, requestCode)
        )
    }

    /**
     * get请求
     */
    fun get(activity: Any?, callback: ICallback) {
        ApiClient.get(activity, "https://www.baidu.com", callback)
    }

    /**
     * post请求
     */
    fun post(activity: Any?, callback: ICallback) {
        ApiClient.post(activity, "post", Bean("100"), callback)
    }
}