package com.lven.retrofit.core

import androidx.collection.ArrayMap
import com.lven.retrofit.api.RestMethod
import com.lven.retrofit.api.RestService
import com.lven.retrofit.callback.ICallback
import com.lven.retrofit.config.RestConfig
import okhttp3.ResponseBody
import retrofit2.Call


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
    fun request(callback: ICallback, service: RestService): Call<ResponseBody> {
        return RestCall(this).request(callback, service)
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

