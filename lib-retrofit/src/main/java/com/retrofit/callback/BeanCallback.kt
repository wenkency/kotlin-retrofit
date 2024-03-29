package com.retrofit.callback

import com.retrofit.core.RestClient
import com.retrofit.core.RestCreator

/**
 * 泛型解析：实际开发自己解析最好
 */
abstract class BeanCallback<T> : ICallback {
    override fun onSuccess(result: String, client: RestClient) {
        // 直接返回String类型
        val type = this.getType()
        val isStr = type.toString() == "${String::class.java}"
        if (isStr) {
            onSucceed(result as T, client)
        } else {
            onSucceed(RestCreator.gson.fromJson(result, type), client)
        }
    }

    abstract fun onSucceed(data: T, client: RestClient)
}

