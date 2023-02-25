package com.retrofit.callback

import android.util.Log
import com.retrofit.config.RestConfig
import com.retrofit.core.RestClient
import com.retrofit.method.RestErrorCode
import com.retrofit.utils.writeToDisk
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.ResponseBody
import java.io.File

/**
 * 处理数据的类
 */
interface IResultCallback {

    fun onResultError(callback: ICallback, download: Boolean, message: String, client: RestClient) {
        callback.onError(RestErrorCode.REST_ERROR, message, client)
        callback.onAfter()
    }

    fun onResultSuccess(
        callback: ICallback,
        download: Boolean,
        responseBody: ResponseBody?,
        client: RestClient
    ) {
        if (responseBody == null) {
            onResultError(callback, download, "ResponseBody is null", client)
            return
        }
        try {
            if (download) {
                Single.just(responseBody)
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
                    .subscribe { file ->
                        callback.onSuccess(file)
                        callback.onAfter()
                    }

            } else {

                Single.just(responseBody)
                    .map {
                        // 请求结果，调用了转换，如果需要，可以配置
                        val result = RestConfig.responseConvertStr(client,it.string())
                        // 打印
                        if (RestConfig.isDebug) {
                            Log.e("Response", client.url)
                            Log.e("Body", result)
                        }

                        result

                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { result ->
                        // 主线程回调
                        callback.onSuccess(result, client)
                        callback.onAfter()
                    }

            }
        } catch (e: Throwable) {
            onResultError(callback, download, "${e.message}", client)
        }
    }

    fun getFileName(): String

    fun getFileDir(): File?
}