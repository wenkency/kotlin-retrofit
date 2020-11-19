package com.lven.retrofit.callback

import android.util.Log
import com.lven.retrofit.api.RestErrorCode
import com.lven.retrofit.config.RestConfig
import com.lven.retrofit.core.RestClient
import com.lven.retrofit.utils.Base64
import com.lven.retrofit.utils.writeToDisk
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.ResponseBody
import java.io.File
import kotlin.coroutines.coroutineContext

interface IResultCallback {

    fun onResultError(callback: ICallback, download: Boolean, message: String,client: RestClient) {
        callback.onError(RestErrorCode.REST_ERROR, message,client)
        callback.onAfter()
    }

    fun onResultSuccess(callback: ICallback, download: Boolean, body: ResponseBody?,client: RestClient) {
        if (body == null) {
            callback.onError(RestErrorCode.BODY_ERROR, "返回空内容",client)
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
                            Log.e("Response",client.url)
                            Log.e("Body", String(Base64.decode(result)))
                        } else {
                            Log.e("Response",client.url)
                            Log.e("Response", result)
                        }
                    }
                    callback.onSuccess(result, client)
                }
            } catch (e: Exception) {
                callback.onError(RestErrorCode.REST_ERROR, e.message ?: "",client)
            }
        }

        callback.onAfter()
    }

    fun getFileName(): String

    fun getFileDir(): File?
}