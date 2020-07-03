package com.lven.retrofit.core

import android.util.Log
import com.lven.retrofit.api.RestMethod
import com.lven.retrofit.callback.ICallback
import com.lven.retrofit.config.RestConfig
import com.lven.retrofit.utils.multipartBody
import com.lven.retrofit.utils.requestBody
import okhttp3.ResponseBody
import retrofit2.Call

class RestCall(private val client: RestClient) {

    /**
     * 发起请求
     */
    fun request(callback: ICallback): Call<ResponseBody> {
        // 调用
        callback.onBefore(client.headers, client.params)
        // 如果是Debug，就打印一下日志
        if (RestConfig.isDebug) {
            Log.e("LOG Url", client.url)
            Log.e("LOG Head", RestCreator.gson.toJson(client.headers))
            Log.e("LOG Params", RestCreator.gson.toJson(client.params))
        }
        val service = RestCreator.getService()

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
}


