package com.lven.retrofit.callback

import com.lven.retrofit.core.RestClient
import com.lven.retrofit.core.RestCreator.gson
import java.io.File

/**
 * Object解析，用于一个页面多个接口
 */
class ObjectCallback(
    private val callback: IObjectCallback? = null,
    private val clazz: Class<*>? = null
) : ICallback {

    override fun onSuccess(response: String, client: RestClient) {
        if (callback == null) {
            return
        }
        try {
            var data: Any? = response
            if (clazz != null) {
                data = if (clazz == String::class.java || clazz == String.javaClass) {
                    response
                } else {
                    gson.fromJson<Any>(response, clazz)
                }
            }
            callback.onSuccess(response, data, clazz)
        } catch (e: Throwable) {
            // 解析失败，原封不动返回
            callback.onSuccess(response, response, clazz)
        }
    }

    override fun onError(code: Int, message: String, client: RestClient) {
        callback?.onError(code, message, clazz)
    }

    override fun onBefore(client: RestClient) {
        callback?.onBefore(client, clazz)
    }

    override fun onAfter() {
        callback?.onAfter(clazz)
    }

    override fun onProgress(progress: Float, current: Float, total: Float) {

    }

    override fun onSuccess(file: File) {

    }

}