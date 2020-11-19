package com.lven.retrofit.callback

import com.lven.retrofit.core.RestClient
import com.lven.retrofit.core.RestCreator

/**
 * 泛型解析：实际开发自己解析最好
 */
abstract class BeanCallback<T> : ICallback {
    override fun onSuccess(result: String, client: RestClient) {
        // 直接返回String类型
        val type = this.getType()
        val isString = type.toString() == "${String::class.java}"
        if (isString) {
            onSucceed(result as T)
        } else {
            onSucceed(RestCreator.gson.fromJson(result, type))
        }
    }

    abstract fun onSucceed(t: T)
}

