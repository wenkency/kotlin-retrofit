package com.lven.retrofit.callback

import java.lang.reflect.GenericArrayType
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.TypeVariable

/**
 * 回调接口
 */
interface ICallback {
    fun onBefore(headers: MutableMap<String, String>) {}

    fun onAfter() {}

    fun onError(code: Int, message: String) {}

    fun onSuccess(result: String) {}

    /**
     * 文件上传的进度
     */
    fun onProgress(progress: Float, current: Float, total: Float) {}

}