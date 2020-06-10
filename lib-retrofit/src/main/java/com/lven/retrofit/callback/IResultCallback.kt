package com.lven.retrofit.callback

import com.lven.retrofit.api.RestErrorCode
import com.lven.retrofit.utils.writeToDisk
import okhttp3.ResponseBody

interface IResultCallback {

    fun onResultError(callback: ICallback, download: Boolean, message: String) {
        callback.onError(RestErrorCode.REST_ERROR, message)
        callback.onAfter()
    }

    fun onResultSuccess(callback: ICallback, download: Boolean, body: ResponseBody) {
        try {
            if (download) {
                var total = body.contentLength()
                var inputStream = body.byteStream()
                writeToDisk(inputStream, getDirName(), getFileName(), callback, total.toFloat())
            } else {
                callback.onSuccess(body.string())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        callback.onAfter()
    }

    fun getFileName(): String

    fun getDirName(): String
}