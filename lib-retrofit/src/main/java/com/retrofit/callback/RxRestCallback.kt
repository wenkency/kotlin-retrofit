package com.retrofit.callback


import com.retrofit.core.RestClient
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import okhttp3.ResponseBody
import java.io.File

/**
 * 请求回调
 */
class RxRestCallback(
    private val callback: ICallback,
    private val download: Boolean,
    private val fileDir: File?,
    private val fileName: String,
    private val client: RestClient
) :
    SingleObserver<ResponseBody>, IResultCallback {

    override fun onSubscribe(d: Disposable) {

    }

    override fun onSuccess(body: ResponseBody) {
        try {
            onResultSuccess(callback, download, body, client)
        } catch (e: Throwable) {
            onError(e)
        }
    }


    override fun onError(e: Throwable) {
        onResultError(callback, download, "${e.message}", client)
    }

    override fun getFileName(): String {
        return fileName
    }

    override fun getFileDir(): File? {
        return fileDir
    }
}