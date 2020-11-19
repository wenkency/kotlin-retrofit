package com.lven.retrofit

import android.app.Activity
import androidx.collection.ArrayMap
import com.lven.retrofit.api.RestMethod
import com.lven.retrofit.api.RestService
import com.lven.retrofit.callback.ICallback
import com.lven.retrofit.core.RestCreator
import java.io.File
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

interface IRetrofit {
    /**
     * 执行方法
     * 子类实现
     */
    fun enqueue(
        activity: Activity? = null,
        method: RestMethod, url: String,
        headers: MutableMap<String, String>? = null,
        params: MutableMap<String, Any>? = null,
        callback: ICallback,
        fileDir: File? = null,
        fileName: String = ""
    )


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

    fun download(
        activity: Activity? = null,
        url: String,
        fileName: String,
        callback: ICallback
    ) {
        download(activity, url, null, null, null, fileName, callback)
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
    for (property in this::class.declaredMemberProperties) {
        property.isAccessible = true
        val kp = property as KProperty1<Any, Any>
        map[kp.name] = kp.get(this)
    }
    return map
}