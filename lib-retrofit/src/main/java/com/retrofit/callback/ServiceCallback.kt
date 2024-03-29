package com.retrofit.callback

import com.retrofit.core.RestClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

/**
 * 普通请求，统一回调处理
 */
class ServiceCallback(
    private val callback: ICallback,
    private val download: Boolean,
    private val fileDir: File?,
    private val fileName: String,
    private val client: RestClient
) :
    Callback<ResponseBody>, IResultCallback {

    // 这个是Retrofit的错误回调
    override fun onFailure(call: Call<ResponseBody>?, e: Throwable) {
        onResultError(callback, download, "${e.message}", client)
    }

    // 这个是Retrofit的成功回调
    override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>) {
        try {
            onResultSuccess(callback, download, response.body(), client)
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