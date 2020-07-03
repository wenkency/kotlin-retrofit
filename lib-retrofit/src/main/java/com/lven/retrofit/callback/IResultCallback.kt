package com.lven.retrofit.callback

import android.util.Log
import com.lven.retrofit.api.RestErrorCode
import com.lven.retrofit.config.RestConfig
import com.lven.retrofit.utils.Base64
import com.lven.retrofit.utils.writeToDisk
import okhttp3.ResponseBody

interface IResultCallback {

    fun onResultError(callback: ICallback, download: Boolean, message: String) {
        callback.onError(RestErrorCode.REST_ERROR, message)
        callback.onAfter()
    }

    fun onResultSuccess(callback: ICallback, download: Boolean, body: ResponseBody?) {
        if (body == null) {
            callback.onError(RestErrorCode.BODY_ERROR, "返回空内容")
            callback.onAfter()
            return
        }
        body?.let {
            try {
                if (download) {
                    var total = it.contentLength()
                    var inputStream = it.byteStream()
                    writeToDisk(inputStream, getDirName(), getFileName(), callback, total.toFloat())
                } else {
                    var result = it.string()
                    if (RestConfig.isDebug) {
                        if (RestConfig.isBase64) {
                            Log.e("LOG Body", String(Base64.decode(result)))
                        } else {
                            Log.e("LOG Body", result)
                        }
                    }
                    callback.onSuccess(result)
                }
            } catch (e: Exception) {
                callback.onError(RestErrorCode.REST_ERROR, e.message ?: "")
            }
        }

        callback.onAfter()
    }

    fun getFileName(): String

    fun getDirName(): String
}