package com.retrofit.core

import android.util.Log
import com.retrofit.api.RestMethod
import com.retrofit.api.RestService
import com.retrofit.api.RxRestService
import com.retrofit.callback.ICallback
import com.retrofit.config.RestConfig
import com.retrofit.utils.multipartBody
import com.retrofit.utils.requestBody
import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody
import retrofit2.Call

class RestCall(private val client: RestClient) {

    /**
     * 发起请求
     */
    fun request(
        callback: ICallback,
        service: RestService
    ): Call<ResponseBody> {
        // 调用
        callback.onBefore(client)
        // 如果是Debug，就打印一下日志
        if (RestConfig.isDebug) {
            Log.e("Request Url", client.url)
            Log.e("Request Head", RestCreator.gson.toJson(client.headers))
            Log.e("Request Params", RestCreator.gson.toJson(client.params))
        }

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
                requestBody(client.params),
                client.tag
            )
            RestMethod.PUT -> service.put(
                client.url,
                client.headers,
                requestBody(client.params),
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
    fun rxRequest(callback: ICallback,service: RxRestService): Single<ResponseBody> {
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
                requestBody(client.params),
                client.tag
            )
            RestMethod.PUT -> service.put(
                client.url,
                client.headers,
                requestBody(client.params),
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


