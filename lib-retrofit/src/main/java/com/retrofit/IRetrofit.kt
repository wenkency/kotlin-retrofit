package com.retrofit

import androidx.collection.ArrayMap
import com.retrofit.api.FieldToJson
import com.retrofit.api.RestMethod
import com.retrofit.api.RestService
import com.retrofit.api.RxRestService
import com.retrofit.callback.ICallback
import com.retrofit.callback.RestCallback
import com.retrofit.callback.RxRestCallback
import com.retrofit.core.RestClient
import com.retrofit.core.RestCreator
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File
import java.lang.reflect.Field

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
            .tag(tag.tag())
            .params(params)
            .build()
        // 这里分支
        if (isRxService()) {
            rxRequest(client, method, callback, fileDir, fileName)
        } else {
            request(client, method, callback, fileDir, fileName)
        }
    }


    private fun rxRequest(
        client: RestClient,
        method: RestMethod,
        callback: ICallback,
        fileDir: File? = null,
        fileName: String = ""
    ) {
        // 回调封装
        val restCallback = RxRestCallback(
            callback,
            method == RestMethod.DOWNLOAD,
            fileDir,
            fileName,
            client
        )
        // 这个也是调起请求
        client.rxRequest(callback, getRxService())
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
        val restCallback = RestCallback(
            callback,
            method == RestMethod.DOWNLOAD,
            fileDir,
            fileName,
            client
        )
        // 发走请求
        client.request(callback, getService()).enqueue(restCallback)
    }

    /**
     * 普通实现
     */
    fun getService(): RestService {
        return RestCreator.getService()
    }

    /**
     * Rx实现
     */
    fun getRxService(): RxRestService {
        return RestCreator.getRxService()
    }

    /**
     * 这个方法控制是不是RX加载
     */
    fun isRxService(): Boolean


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
        get(any, url, null, any.anyToMap(), callback)
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
        post(any, url, null, any.anyToMap(), callback)
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

    fun postForm(tag: Any? = null, url: String, any: Any, callback: ICallback) {
        postForm(any, url, null, any.anyToMap(), callback)
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
        put(any, url, null, any.anyToMap(), callback)
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
        putForm(any, url, null, any.anyToMap(), callback)
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
        delete(any, url, null, any.anyToMap(), callback)
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
    return "${this::class.simpleName}_${this.hashCode()}"
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
    var allFields = getAllFields(this)
    allFields.forEach { field ->
        try {
            val name = field.name
            if (!name.contains("$") && name != "serialVersionUID") {
                field.isAccessible = true
                val value = field[this]
                val fieldToJson: FieldToJson? = field.getAnnotation(FieldToJson::class.java)
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

fun getAllFields(obj: Any): List<Field> {
    var clazz: Class<*>? = obj.javaClass
    val fieldList: MutableList<Field> = ArrayList()
    while (clazz != null) {
        fieldList.addAll(ArrayList(listOf(*clazz.declaredFields)))
        clazz = clazz.superclass
    }
    return fieldList
}