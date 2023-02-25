package com.retrofit.callback


import com.retrofit.config.RxCancelUtils
import com.retrofit.core.RestClient
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import okhttp3.ResponseBody
import java.io.File

/**
 * Rx请求，统一回调处理
 */
class RxServiceCallback(
    private val callback: ICallback,
    private val download: Boolean,
    private val fileDir: File?,
    private val fileName: String,
    private val client: RestClient,
    private val tag: String?
) :
    SingleObserver<ResponseBody>, IResultCallback {
    private var disposable: Disposable? = null
    override fun onSubscribe(d: Disposable) {
        // 取消设置 一个tag 多个 disposable
        tag?.let {
            disposable = d
            RxCancelUtils.add(it, d)
        }
    }

    override fun onSuccess(body: ResponseBody) {
        // 订阅流程走完了，马上移除Disposable
        cancel()
        // 再回调
        onResultSuccess(callback, download, body, client)
    }


    override fun onError(e: Throwable) {
        // 订阅流程走完了，马上移除Disposable
        cancel()
        // 再回调
        onResultError(callback, download, "${e.message}", client)
    }

    private fun cancel() {
        tag?.let { t ->
            disposable?.let { d ->
                RxCancelUtils.remove(t, d)
            }
        }
    }

    override fun getFileName(): String {
        return fileName
    }

    override fun getFileDir(): File? {
        return fileDir
    }
}