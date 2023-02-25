package com.retrofit.callback

import com.retrofit.core.RestClient

/**
 * 目的是一个页面，多个接口在一个方法回调
 */
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