package com.lven.retrofitkotlin.presenter

import com.retrofit.RetrofitPresenter
import com.retrofit.callback.ICallback
import com.retrofit.callback.IObjectCallback
import com.retrofit.callback.ObjectCallback
import com.retrofit.utils.RestUtils
import com.lven.retrofitkotlin.bean.Bean

object MainPresenter {
    /**
     * Object回调
     */
    fun postObj(
        activity: Any?, callback: IObjectCallback,
        clazz: Class<*>, requestCode: Int = 1
    ) {
        val map = RestUtils.getParams()
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
        RetrofitPresenter.get(activity, "https://www.baidu.com", callback)
    }

    /**
     * post请求
     */
    fun post(activity: Any?, callback: ICallback) {
        RetrofitPresenter.post(activity, "post", Bean("100"), callback)
    }
}