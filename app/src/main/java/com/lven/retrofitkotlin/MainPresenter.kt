package com.lven.retrofitkotlin

import android.app.Activity
import com.lven.retrofit.RetrofitPresenter
import com.lven.retrofit.callback.BeanCallback
import com.lven.retrofit.callback.ICallback
import com.lven.retrofit.callback.IObjectCallback
import com.lven.retrofit.callback.ObjectCallback

object MainPresenter {
    /**
     * Object回调
     */
    fun postObj(activity: Activity?, callback: IObjectCallback, clazz: Class<*>) {
        MultiUrlPresenter.post(
            activity,
            "post",
            Bean("100"),
            ObjectCallback(callback, clazz)
        )
    }

    /**
     * get请求
     */
    fun get(activity: Activity?, callback: ICallback) {
        RetrofitPresenter.get(activity, "https://www.baidu.com", callback)
    }
    /**
     * post请求
     */
    fun post(activity: Activity?, callback: ICallback) {
        RetrofitPresenter.post(activity, "post", Bean("100"), callback)
    }
}