package com.lven.retrofit

import android.app.Activity
import androidx.collection.ArrayMap
import com.lven.retrofit.api.FieldToJson
import com.lven.retrofit.api.RestMethod
import com.lven.retrofit.api.RestService
import com.lven.retrofit.callback.ICallback
import com.lven.retrofit.callback.RestCallback
import com.lven.retrofit.core.RestClient
import com.lven.retrofit.core.RestCreator
import java.io.File
import java.lang.reflect.Field
import java.util.*

/**
 * APP网络调用统一封装
 */
interface IRetrofit {
    /**
     * 执行方法
     */
    fun enqueue(
        activity: Activity? = null,
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
            .tag(activity.tag())
            .params(params)
            .build()

        // 回调封装
        val restCallback = RestCallback(
            callback,
            method == RestMethod.DOWNLOAD,
            fileDir,
            fileName,
            client
        )
        client.request(callback, getService()).enqueue(restCallback)
    }

    /**
     * 重写这个方法就可以更新BaseURL
     */
    fun getService(): RestService


    // == get method===================================================================================
    fun get(
        activity: Activity? = null,
        url: String,
        headers: MutableMap<String, String>? = null,
        params: MutableMap<String, Any>? = null,
        callback: ICallback
    ) {
        enqueue(activity, RestMethod.GET, url, headers, params, callback)
    }

    fun get(
        activity: Activity? = null,
        url: String,
        params: MutableMap<String, Any>,
        callback: ICallback
    ) {
        get(activity, url, null, params, callback)
    }

    fun get(activity: Activity? = null, url: String, callback: ICallback) {
        get(activity, url, null, null, callback)
    }

    fun get(activity: Activity? = null, url: String, callback: ICallback, vararg params: String) {
        get(activity, url, null, params.arrayToMap(), callback)
    }

    fun get(activity: Activity? = null, url: String, any: Any, callback: ICallback) {
        get(activity, url, null, any.anyToMap(), callback)
    }

    // == post method===================================================================================
    fun post(
        activity: Activity? = null,
        url: String,
        headers: MutableMap<String, String>? = null,
        params: MutableMap<String, Any>? = null,
        callback: ICallback
    ) {
        enqueue(activity, RestMethod.POST, url, headers, params, callback)
    }

    fun post(
        activity: Activity? = null,
        url: String,
        params: MutableMap<String, Any>,
        callback: ICallback
    ) {
        post(activity, url, null, params, callback)
    }

    fun post(activity: Activity? = null, url: String, callback: ICallback) {
        post(activity, url, null, null, callback)
    }

    fun post(activity: Activity? = null, url: String, callback: ICallback, vararg params: String) {
        post(activity, url, null, params.arrayToMap(), callback)
    }

    fun post(activity: Activity? = null, url: String, any: Any, callback: ICallback) {
        post(activity, url, null, any.anyToMap(), callback)
    }

    // == postForm method===================================================================================
    fun postForm(
        activity: Activity? = null,
        url: String,
        headers: MutableMap<String, String>? = null,
        params: MutableMap<String, Any>? = null,
        callback: ICallback
    ) {
        enqueue(activity, RestMethod.POST_FORM, url, headers, params, callback)
    }

    fun postForm(
        activity: Activity? = null,
        url: String,
        params: MutableMap<String, Any>,
        callback: ICallback
    ) {
        postForm(activity, url, null, params, callback)
    }

    fun postForm(activity: Activity? = null, url: String, callback: ICallback) {
        postForm(activity, url, null, null, callback)
    }

    fun postForm(
        activity: Activity? = null,
        url: String,
        callback: ICallback,
        vararg params: String
    ) {
        postForm(activity, url, null, params.arrayToMap(), callback)
    }

    fun postForm(activity: Activity? = null, url: String, any: Any, callback: ICallback) {
        postForm(activity, url, null, any.anyToMap(), callback)
    }

    // == put method===================================================================================
    fun put(
        activity: Activity? = null,
        url: String,
        headers: MutableMap<String, String>? = null,
        params: MutableMap<String, Any>? = null,
        callback: ICallback
    ) {
        enqueue(activity, RestMethod.PUT, url, headers, params, callback)
    }

    fun put(
        activity: Activity? = null,
        url: String,
        params: MutableMap<String, Any>,
        callback: ICallback
    ) {
        put(activity, url, null, params, callback)
    }

    fun put(activity: Activity? = null, url: String, callback: ICallback) {
        put(activity, url, null, null, callback)
    }

    fun put(
        activity: Activity? = null,
        url: String,
        callback: ICallback,
        vararg params: String
    ) {
        put(activity, url, null, params.arrayToMap(), callback)
    }

    fun put(activity: Activity? = null, url: String, any: Any, callback: ICallback) {
        put(activity, url, null, any.anyToMap(), callback)
    }

    // == putForm method===================================================================================
    fun putForm(
        activity: Activity? = null,
        url: String,
        headers: MutableMap<String, String>? = null,
        params: MutableMap<String, Any>? = null,
        callback: ICallback
    ) {
        enqueue(activity, RestMethod.PUT_FORM, url, headers, params, callback)
    }

    fun putForm(
        activity: Activity? = null,
        url: String,
        params: MutableMap<String, Any>,
        callback: ICallback
    ) {
        putForm(activity, url, null, params, callback)
    }

    fun putForm(activity: Activity? = null, url: String, callback: ICallback) {
        putForm(activity, url, null, null, callback)
    }

    fun putForm(
        activity: Activity? = null,
        url: String,
        callback: ICallback,
        vararg params: String
    ) {
        putForm(activity, url, null, params.arrayToMap(), callback)
    }

    fun putForm(activity: Activity? = null, url: String, any: Any, callback: ICallback) {
        putForm(activity, url, null, any.anyToMap(), callback)
    }

    // == delete method===================================================================================
    fun delete(
        activity: Activity? = null,
        url: String,
        headers: MutableMap<String, String>? = null,
        params: MutableMap<String, Any>? = null,
        callback: ICallback
    ) {
        enqueue(activity, RestMethod.DELETE, url, headers, params, callback)
    }
    fun delete(
        activity: Activity? = null,
        url: String,
        params: MutableMap<String, Any>,
        callback: ICallback
    ) {
        delete(activity, url, null, params, callback)
    }
    fun delete(activity: Activity? = null, url: String, callback: ICallback) {
        delete(activity, url, null, null, callback)
    }

    fun delete(
        activity: Activity? = null,
        url: String,
        callback: ICallback,
        vararg params: String
    ) {
        delete(activity, url, null, params.arrayToMap(), callback)
    }

    fun delete(activity: Activity? = null, url: String, any: Any, callback: ICallback) {
        delete(activity, url, null, any.anyToMap(), callback)
    }

    // == upload method===================================================================================
    fun upload(
        activity: Activity? = null,
        url: String,
        headers: MutableMap<String, String>? = null,
        params: MutableMap<String, Any>? = null,
        callback: ICallback
    ) {
        enqueue(activity, RestMethod.UPLOAD, url, headers, params, callback)
    }

    fun upload(
        activity: Activity? = null,
        url: String,
        params: MutableMap<String, Any>? = null,
        callback: ICallback
    ) {
        upload(activity, url, null, params, callback)
    }

    // == download method===================================================================================
    fun download(
        activity: Activity? = null,
        url: String,
        headers: MutableMap<String, String>? = null,
        params: MutableMap<String, Any>? = null,
        fileDir: File?,
        fileName: String,
        callback: ICallback
    ) {
        enqueue(activity, RestMethod.DOWNLOAD, url, headers, params, callback, fileDir, fileName)
    }

    /**
     * @param fileDir 下载文件放哪个目录
     * @param fileName 文件名称
     */
    fun download(
        activity: Activity? = null,
        url: String,
        fileDir: File?,
        fileName: String,
        callback: ICallback
    ) {
        download(activity, url, null, null, fileDir, fileName, callback)
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