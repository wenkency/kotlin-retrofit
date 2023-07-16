package com.retrofit

import com.retrofit.api.ApiService
import com.retrofit.core.RestClient

/**
 * 普通网络请求,如果有扩展就继承IRetrofit，参照这个类来写
 * 比如有多个BaseURL，重写getService()方法就可以了
 */
object ApiClient : IRetrofit {
    // 是不是Rx方式
    override fun isRxService(): Boolean {
        return false
    }

    // 是不是协程方式
    override fun isSuspendService(): Boolean {
        return false
    }

    // 创建服务
    override fun getService(): ApiService {
        return super.getService()
    }

    // 公共请求头
    override fun commHeaders(): MutableMap<String, String>? {
        return null
    }

    // 公共请求参数
    override fun commParams(): MutableMap<String, Any>? {
        return null
    }

    // 请求转换
    override fun requestConvert(client: RestClient, data: String): String {
        // 有特殊需求，比如要Base64加密，就可以在这里统一处理
        // client 处理一些特殊的请求，要用client判断
        return data
    }

    // 响应转换
    override fun responseConvert(client: RestClient, data: String): String {
        // 有特殊需求，比如要Base64解密，就可以在这里统一处理
        // client 处理一些特殊的请求，要用client判断
        return data
    }
}

