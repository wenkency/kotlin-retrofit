package com.lven.retrofit.callback

import com.lven.retrofit.core.RestClient

interface IObjectCallback : ICallback {

    fun onBefore(client: RestClient, clazz: Class<*>?, requestCode: Int = -1)

    fun onAfter(clazz: Class<*>?, requestCode: Int = -1)

    /**
     * 请求成功回调事件处理
     * @param json 返回的Json
     * @param data 默认返回空，要自己解析
     */
    fun onSuccess(json: String, data: Any?, clazz: Class<*>?, requestCode: Int = -1)

    /**
     * 请求失败回调事件处理
     */
    fun onError(code: Int, message: String, clazz: Class<*>?, requestCode: Int = -1)
}