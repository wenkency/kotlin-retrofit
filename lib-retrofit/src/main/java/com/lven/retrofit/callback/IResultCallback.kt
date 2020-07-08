package com.lven.retrofit.callback

import android.util.Log
import com.lven.retrofit.api.RestErrorCode
import com.lven.retrofit.config.RestConfig
import com.lven.retrofit.utils.Base64
import com.lven.retrofit.utils.writeToDisk
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.ResponseBody
import java.io.File

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

                    Single.just(it)
                        .map { body ->
                            val total = body.contentLength()
                            val inputStream = body.byteStream()
                            // 进度回调在里面
                            writeToDisk(
                                inputStream,
                                getFileDir(),
                                getFileName(),
                                callback,
                                total.toFloat()
                            )
                        }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(Consumer { file ->
                            callback.onSuccess(file)
                        })

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

    fun getFileDir(): File?
}