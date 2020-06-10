package com.lven.retrofit.callback



import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import okhttp3.ResponseBody

/**
 * 请求回调
 */
class RxRestCallback(
    private val callback: ICallback,
    private val download: Boolean,
    private val dirName: String = "",
    private val fileName: String = ""
) :
    SingleObserver<ResponseBody>, IResultCallback {

    override fun onSubscribe(d: Disposable) {

    }

    override fun onSuccess(body: ResponseBody) {
        onResultSuccess(callback, download, body)
    }


    override fun onError(e: Throwable) {
        onResultError(callback, download, "${e.message}")
    }

    override fun getFileName(): String {
        return fileName
    }

    override fun getDirName(): String {
        return dirName
    }
}