package com.lven.retrofit.callback

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

/**
 * 请求回调
 */
class RestCallback(
    private val callback: ICallback,
    private val download: Boolean,
    private val fileDir: File?,
    private val fileName: String
) :
    Callback<ResponseBody>, IResultCallback {
    override fun onFailure(call: Call<ResponseBody>, e: Throwable) {
        onResultError(callback, download, "${e.message}")
    }

    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
        try {
            onResultSuccess(callback, download, response.body())
        } catch (e: Throwable) {
            onFailure(call, e)
        }
    }

    override fun getFileName(): String {
        return fileName
    }

    override fun getFileDir(): File? {
        return fileDir
    }
}