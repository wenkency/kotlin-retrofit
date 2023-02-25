package com.retrofit.core

import com.retrofit.api.RxService
import com.retrofit.api.ApiService
import com.retrofit.api.SuspendService
import com.retrofit.callback.ICallback
import com.retrofit.method.RestMethod
import com.retrofit.utils.multipartBody
import com.retrofit.utils.requestBody
import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class RestCall(private val client: RestClient) {

    private fun onBefore(callback: ICallback) {
        callback.onBefore(client)
    }

    /**
     * 发起请求
     */
    fun request(
        callback: ICallback,
        service: ApiService
    ): Call<ResponseBody> {
        // 调用 onBefore
        onBefore(callback)

        return when (client.method) {
            RestMethod.GET -> service.get(client.url, client.headers, client.params, client.tag)
            RestMethod.DELETE -> service.delete(
                client.url,
                client.headers,
                client.params,
                client.tag
            )
            RestMethod.POST_FORM -> service.postForm(
                client.url,
                client.headers,
                client.params,
                client.tag
            )
            RestMethod.PUT_FORM -> service.putForm(
                client.url,
                client.headers,
                client.params,
                client.tag
            )
            RestMethod.POST -> service.post(
                client.url,
                client.headers,
                requestBody(client),
                client.tag
            )
            RestMethod.PUT -> service.put(
                client.url,
                client.headers,
                requestBody(client),
                client.tag
            )
            RestMethod.UPLOAD -> service.upload(
                client.url,
                client.headers,
                multipartBody(client.params, callback),
                client.tag
            )
            RestMethod.DOWNLOAD -> service.download(
                client.url,
                client.headers,
                client.params,
                client.tag
            )
        }
    }

    /**
     * 发起请求
     */
    suspend fun suspendRequest(
        callback: ICallback,
        service: SuspendService
    ): Response<ResponseBody> {
        // 调用 onBefore
        onBefore(callback)

        return when (client.method) {
            RestMethod.GET -> service.get(client.url, client.headers, client.params, client.tag)
            RestMethod.DELETE -> service.delete(
                client.url,
                client.headers,
                client.params,
                client.tag
            )
            RestMethod.POST_FORM -> service.postForm(
                client.url,
                client.headers,
                client.params,
                client.tag
            )
            RestMethod.PUT_FORM -> service.putForm(
                client.url,
                client.headers,
                client.params,
                client.tag
            )
            RestMethod.POST -> service.post(
                client.url,
                client.headers,
                requestBody(client),
                client.tag
            )
            RestMethod.PUT -> service.put(
                client.url,
                client.headers,
                requestBody(client),
                client.tag
            )
            RestMethod.UPLOAD -> service.upload(
                client.url,
                client.headers,
                multipartBody(client.params, callback),
                client.tag
            )
            RestMethod.DOWNLOAD -> service.download(
                client.url,
                client.headers,
                client.params,
                client.tag
            )
        }
    }

    /**
     * 发起RX请求
     */
    fun rxRequest(callback: ICallback, service: RxService): Single<ResponseBody> {
        // 调用 onBefore
        onBefore(callback)
        return when (client.method) {
            RestMethod.GET -> service.get(client.url, client.headers, client.params, client.tag)
            RestMethod.DELETE -> service.delete(
                client.url,
                client.headers,
                client.params,
                client.tag
            )
            RestMethod.POST_FORM -> service.postForm(
                client.url,
                client.headers,
                client.params,
                client.tag
            )
            RestMethod.PUT_FORM -> service.putForm(
                client.url,
                client.headers,
                client.params,
                client.tag
            )
            RestMethod.DOWNLOAD -> service.download(
                client.url,
                client.headers,
                client.params,
                client.tag
            )
            RestMethod.POST -> service.post(
                client.url,
                client.headers,
                requestBody(client),
                client.tag
            )
            RestMethod.PUT -> service.put(
                client.url,
                client.headers,
                requestBody(client),
                client.tag
            )
            RestMethod.UPLOAD -> service.upload(
                client.url,
                client.headers,
                multipartBody(client.params, callback),
                client.tag
            )
        }
    }

}


