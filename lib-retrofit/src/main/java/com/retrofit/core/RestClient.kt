package com.retrofit.core

import androidx.collection.ArrayMap
import com.retrofit.api.RxService
import com.retrofit.api.ApiService
import com.retrofit.api.SuspendService
import com.retrofit.callback.ICallback
import com.retrofit.config.RestConfig
import com.retrofit.method.RestMethod
import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response


/**
 * API 客户端，请求的信息都在这里
 */
class RestClient(builder: RestfulBuilder) {
    val url: String = builder.url
    val headers: MutableMap<String, String> = builder.headers
    val params: MutableMap<String, Any> = builder.params
    val tag: String = builder.tag
    val method: RestMethod = builder.method

    init {
        // 统一添加公共头
        var commHeaders = RestConfig.commHeaders
        if (commHeaders.isNotEmpty()) {
            this.headers += commHeaders
        }
        // 统一添加公共参数
        var commParams = RestConfig.commParams
        if (commParams.isNotEmpty()) {
            this.params += commHeaders
        }
    }

    /**
     * 发起请求
     */
    fun request(callback: ICallback, service: ApiService): Call<ResponseBody> {
        return RestCall(this).request(callback, service)
    }

    /**
     * 协程方式发起请求
     */
    suspend fun request(callback: ICallback, service: SuspendService): Response<ResponseBody> {
        return RestCall(this).suspendRequest(callback, service)
    }

    /**
     * Rxjava方式发起请求
     */
    fun request(callback: ICallback, service: RxService): Single<ResponseBody> {
        return RestCall(this).rxRequest(callback, service)
    }

    class RestfulBuilder {
        internal var url: String = ""
        internal var tag: String = ""
        internal var method = RestMethod.POST
        internal val headers = ArrayMap<String, String>()
        internal val params = ArrayMap<String, Any>()

        fun url(url: String) = apply {
            this.url = url
        }

        fun tag(tag: String?) = apply {
            if (tag != null) {
                this.tag = tag
            }
        }

        fun method(method: RestMethod) = apply {
            this.method = method
        }

        fun headers(headers: MutableMap<String, String>?) = apply {
            if (headers != null) {
                this.headers += headers
            }
        }

        fun params(params: MutableMap<String, Any>?) = apply {
            if (params != null) {
                this.params += params
            }
        }

        fun build(): RestClient {
            return RestClient(this)
        }
    }
}

