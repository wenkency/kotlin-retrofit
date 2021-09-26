package com.lven.retrofit.callback

import com.lven.retrofit.core.RestClient

interface IObjectCallback : ICallback {

    fun onBefore(client: RestClient, clazz: Class<*>?)

    fun onAfter(clazz: Class<*>?)

    /**
     * 请求成功回调事件处理
     * @param json 返回的Json
     * @param data 默认返回空，要自己解析
     */
    fun onSuccess(json: String, data: Any?, clazz: Class<*>?)

    /**
     * 请求失败回调事件处理
     */
    fun onError(code: Int, message: String, clazz: Class<*>?)
}