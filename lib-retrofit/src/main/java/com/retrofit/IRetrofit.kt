package com.retrofit

import android.util.Log
import androidx.collection.ArrayMap
import com.retrofit.api.ApiService
import com.retrofit.api.RxService
import com.retrofit.api.SuspendService
import com.retrofit.callback.ICallback
import com.retrofit.callback.RxServiceCallback
import com.retrofit.callback.ServiceCallback
import com.retrofit.config.RestConfig
import com.retrofit.cancel.SuspendCancelUtils
import com.retrofit.core.RestClient
import com.retrofit.core.RestCreator
import com.retrofit.api.FieldToJson
import com.retrofit.method.RestMethod
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.launch
import java.io.File
import java.lang.reflect.Field
import java.util.concurrent.CancellationException

/**
 * APP网络调用统一封装
 */
interface IRetrofit {

    fun enqueue(
        tag: Any? = null,
        method: RestMethod, url: String,
        headers: MutableMap<String, String>? = null,
        params: MutableMap<String, Any>? = null,
        callback: ICallback,
        fileDir: File? = null,
        fileName: String = ""
    ) {
        // 请求的Client
        val client = RestClient.RestfulBuilder()
            .method(method)
            .url(url)
            .headers(headers)
            .headers(commHeaders())// 公共请求头
            .params(params)
            .params(commParams())// 公共请求参数
            .tag(tag?.tag())
            .requestConvert(::requestConvert) // 请求转换
            .responseConvert(::responseConvert)// 响应转换
            .build()
        // 这里分支
        if (isRxService()) {
            // rxjava
            rxRequest(client, method, callback, fileDir, fileName, tag.tag())
        } else if (isSuspendService()) {
            // 协程
            suspendRequest(client, method, callback, fileDir, fileName, tag)
        } else {
            // 正常线程
            request(client, method, callback, fileDir, fileName)
        }
    }

    private fun suspendRequest(
        client: RestClient,
        method: RestMethod,
        callback: ICallback,
        fileDir: File? = null,
        fileName: String = "",
        tag: Any?
    ) {
        // 根据TAG
        val scope = SuspendCancelUtils.get(tag)
        scope.launch {
            // 回调封装
            val serviceCallback = ServiceCallback(
                callback,
                method == RestMethod.DOWNLOAD,
                fileDir,
                fileName,
                client
            )
            try {
                val response = client.request(callback, getSuspendService())
                serviceCallback.onResponse(null, response)
            } catch (e: CancellationException) {
                if (RestConfig.isDebug) {
                    Log.e("TAG", "${e.message}")
                }
            } catch (e: Throwable) {
                serviceCallback.onFailure(null, e)
            }
        }

    }

    private fun rxRequest(
        client: RestClient,
        method: RestMethod,
        callback: ICallback,
        fileDir: File? = null,
        fileName: String = "",
        tag: String?
    ) {
        // 回调封装
        val restCallback = RxServiceCallback(
            callback,
            method == RestMethod.DOWNLOAD,
            fileDir,
            fileName,
            client,
            tag
        )
        // 这个也是调起请求
        client.request(callback, getRxService())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(restCallback)
    }

    private fun request(
        client: RestClient,
        method: RestMethod,
        callback: ICallback,
        fileDir: File? = null,
        fileName: String = ""
    ) {
        // 回调封装
        val serviceCallback = ServiceCallback(
            callback,
            method == RestMethod.DOWNLOAD,
            fileDir,
            fileName,
            client
        )
        // 发走请求
        client.request(callback, getService()).enqueue(serviceCallback)
    }

    /**
     * 普通实现
     */
    fun getService(): ApiService {
        return RestCreator.getService()
    }

    /**
     * 协程实现的服务
     */
    fun getSuspendService(): SuspendService {
        return RestCreator.getSuspendService()
    }

    /**
     * Rx实现
     */
    fun getRxService(): RxService {
        return RestCreator.getRxService()
    }

    /**
     * 这个方法控制是不是RX加载
     */
    fun isRxService(): Boolean

    /**
     * 是不是协程方式
     */
    fun isSuspendService(): Boolean

    /**
     * 公共请求头
     */
    fun commHeaders(): MutableMap<String, String>? {
        return null
    }

    /**
     * 公共请求参数
     */
    fun commParams(): MutableMap<String, Any>? {
        return null
    }

    /**
     * 请求转换
     */
    fun requestConvert(client: RestClient, data: String): String {
        return data
    }

    /**
     * 响应转换
     */
    fun responseConvert(client: RestClient, data: String): String {
        return data
    }


    // == get method===================================================================================
    fun get(
        tag: Any? = null,
        url: String,
        headers: MutableMap<String, String>? = null,
        params: MutableMap<String, Any>? = null,
        callback: ICallback
    ) {
        enqueue(tag, RestMethod.GET, url, headers, params, callback)
    }

    fun get(
        tag: Any? = null,
        url: String,
        params: MutableMap<String, Any>,
        callback: ICallback
    ) {
        get(tag, url, null, params, callback)
    }

    fun get(tag: Any? = null, url: String, callback: ICallback) {
        get(tag, url, null, null, callback)
    }

    fun get(tag: Any? = null, url: String, callback: ICallback, vararg params: String) {
        get(tag, url, null, params.arrayToMap(), callback)
    }

    fun get(tag: Any? = null, url: String, any: Any, callback: ICallback) {
        get(tag, url, null, any.anyToMap(), callback)
    }

    // == post method===================================================================================
    fun post(
        tag: Any? = null,
        url: String,
        headers: MutableMap<String, String>? = null,
        params: MutableMap<String, Any>? = null,
        callback: ICallback
    ) {
        enqueue(tag, RestMethod.POST, url, headers, params, callback)
    }

    fun post(
        tag: Any? = null,
        url: String,
        params: MutableMap<String, Any>,
        callback: ICallback
    ) {
        post(tag, url, null, params, callback)
    }

    fun post(tag: Any? = null, url: String, callback: ICallback) {
        post(tag, url, null, null, callback)
    }

    fun post(tag: Any? = null, url: String, callback: ICallback, vararg params: String) {
        post(tag, url, null, params.arrayToMap(), callback)
    }

    fun post(tag: Any? = null, url: String, any: Any, callback: ICallback) {
        post(tag, url, null, any.anyToMap(), callback)
    }

    // == postForm method===================================================================================
    fun postForm(
        tag: Any? = null,
        url: String,
        headers: MutableMap<String, String>? = null,
        params: MutableMap<String, Any>? = null,
        callback: ICallback
    ) {
        enqueue(tag, RestMethod.POST_FORM, url, headers, params, callback)
    }

    fun postForm(
        tag: Any? = null,
        url: String,
        params: MutableMap<String, Any>,
        callback: ICallback
    ) {
        postForm(tag, url, null, params, callback)
    }

    fun postForm(tag: Any? = null, url: String, callback: ICallback) {
        postForm(tag, url, null, null, callback)
    }

    fun postForm(
        tag: Any? = null,
        url: String,
        callback: ICallback,
        vararg params: String
    ) {
        postForm(tag, url, null, params.arrayToMap(), callback)
    }

    fun postForm(tag: Any? = null, url: String, data: Any, callback: ICallback) {
        postForm(tag, url, null, data.anyToMap(), callback)
    }

    // == put method===================================================================================
    fun put(
        tag: Any? = null,
        url: String,
        headers: MutableMap<String, String>? = null,
        params: MutableMap<String, Any>? = null,
        callback: ICallback
    ) {
        enqueue(tag, RestMethod.PUT, url, headers, params, callback)
    }

    fun put(
        tag: Any? = null,
        url: String,
        params: MutableMap<String, Any>,
        callback: ICallback
    ) {
        put(tag, url, null, params, callback)
    }

    fun put(tag: Any? = null, url: String, callback: ICallback) {
        put(tag, url, null, null, callback)
    }

    fun put(
        tag: Any? = null,
        url: String,
        callback: ICallback,
        vararg params: String
    ) {
        put(tag, url, null, params.arrayToMap(), callback)
    }

    fun put(tag: Any? = null, url: String, any: Any, callback: ICallback) {
        put(tag, url, null, any.anyToMap(), callback)
    }

    // == putForm method===================================================================================
    fun putForm(
        tag: Any? = null,
        url: String,
        headers: MutableMap<String, String>? = null,
        params: MutableMap<String, Any>? = null,
        callback: ICallback
    ) {
        enqueue(tag, RestMethod.PUT_FORM, url, headers, params, callback)
    }

    fun putForm(
        tag: Any? = null,
        url: String,
        params: MutableMap<String, Any>,
        callback: ICallback
    ) {
        putForm(tag, url, null, params, callback)
    }

    fun putForm(tag: Any? = null, url: String, callback: ICallback) {
        putForm(tag, url, null, null, callback)
    }

    fun putForm(
        tag: Any? = null,
        url: String,
        callback: ICallback,
        vararg params: String
    ) {
        putForm(tag, url, null, params.arrayToMap(), callback)
    }

    fun putForm(tag: Any? = null, url: String, any: Any, callback: ICallback) {
        putForm(tag, url, null, any.anyToMap(), callback)
    }

    // == delete method===================================================================================
    fun delete(
        tag: Any? = null,
        url: String,
        headers: MutableMap<String, String>? = null,
        params: MutableMap<String, Any>? = null,
        callback: ICallback
    ) {
        enqueue(tag, RestMethod.DELETE, url, headers, params, callback)
    }

    fun delete(
        tag: Any? = null,
        url: String,
        params: MutableMap<String, Any>,
        callback: ICallback
    ) {
        delete(tag, url, null, params, callback)
    }

    fun delete(tag: Any? = null, url: String, callback: ICallback) {
        delete(tag, url, null, null, callback)
    }

    fun delete(
        tag: Any? = null,
        url: String,
        callback: ICallback,
        vararg params: String
    ) {
        delete(tag, url, null, params.arrayToMap(), callback)
    }

    fun delete(tag: Any? = null, url: String, any: Any, callback: ICallback) {
        delete(tag, url, null, any.anyToMap(), callback)
    }

    // == upload method===================================================================================
    fun upload(
        tag: Any? = null,
        url: String,
        headers: MutableMap<String, String>? = null,
        params: MutableMap<String, Any>? = null,
        callback: ICallback
    ) {
        enqueue(tag, RestMethod.UPLOAD, url, headers, params, callback)
    }

    fun upload(
        tag: Any? = null,
        url: String,
        params: MutableMap<String, Any>? = null,
        callback: ICallback
    ) {
        upload(tag, url, null, params, callback)
    }

    // == download method===================================================================================
    fun download(
        tag: Any? = null,
        url: String,
        headers: MutableMap<String, String>? = null,
        params: MutableMap<String, Any>? = null,
        fileDir: File?,
        fileName: String,
        callback: ICallback
    ) {
        enqueue(tag, RestMethod.DOWNLOAD, url, headers, params, callback, fileDir, fileName)
    }

    /**
     * @param fileDir 下载文件放哪个目录
     * @param fileName 文件名称
     */
    fun download(
        tag: Any? = null,
        url: String,
        fileDir: File?,
        fileName: String,
        callback: ICallback
    ) {
        download(tag, url, null, null, fileDir, fileName, callback)
    }
}


/**
 * 获取任意对象的TAG
 */
fun Any?.tag(): String? {
    if (this == null) {
        return null
    }
    return "${this::class.simpleName}@${this.hashCode()}"
}

/**
 * List转成Map
 */
fun Array<out String>.arrayToMap(): MutableMap<String, Any> {
    val map = ArrayMap<String, Any>()
    var key = ""
    this.forEachIndexed { index, item ->
        if (index % 2 == 0) {
            key = item
        } else {
            map[key] = item
        }
    }
    return map
}

/**
 * 对象转成Map
 */
fun Any.anyToMap(): MutableMap<String, Any> {
    val map = ArrayMap<String, Any>()
    var allFields = this.getAllFields()
    allFields.forEach { field ->
        try {
            val name = field.name
            if (!name.contains("$") && name != "serialVersionUID") {
                field.isAccessible = true
                val value = field[this]
                val fieldToJson: FieldToJson? = field.getAnnotation(
                    FieldToJson::class.java
                )
                if (fieldToJson != null) {
                    map[name] = RestCreator.gson.toJson(value)
                } else {
                    map[name] = value
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
    return map
}

fun Any.getAllFields(): List<Field> {
    var clazz: Class<*>? = this.javaClass
    val fieldList: MutableList<Field> = ArrayList()
    // 这里的父类，去除Object
    while (clazz != null && clazz != Object::class.java) {
        fieldList.addAll(ArrayList(listOf(*clazz.declaredFields)))
        clazz = clazz.superclass
    }
    return fieldList
}