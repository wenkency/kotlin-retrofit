package com.lven.retrofit.callback

import java.io.File

/**
 * 回调接口
 */
interface ICallback {
    fun onBefore(headers: MutableMap<String, String>, params: MutableMap<String, Any>) {}

    fun onAfter() {}

    fun onError(code: Int, message: String) {}

    fun onSuccess(result: String) {}

    /**
     * 文件上传的进度
     */
    fun onProgress(progress: Float, current: Float, total: Float) {}

    /**
     * 文件上下载成功
     */
    fun onSuccess(file: File) {

    }

}