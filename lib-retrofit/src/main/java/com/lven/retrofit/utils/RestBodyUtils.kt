package com.lven.retrofit.utils

import android.text.TextUtils
import com.lven.retrofit.callback.ICallback
import com.lven.retrofit.config.RestConfig
import com.lven.retrofit.core.RestCreator
import com.lven.retrofit.core.RestMultipartBody
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.net.URLConnection

/**
 * 创建请求体，针对下载回调请求
 */
fun multipartBody(params: MutableMap<String, Any>, callback: ICallback): MultipartBody {
    // 多文本二进制表单
    var builder = MultipartBody.Builder().setType(MultipartBody.FORM)
    for ((key, value) in params) {
        if (TextUtils.isEmpty(key) || value == null) {
            continue
        }
        when (value) {
            is File -> {
                fileToBody(builder, key, value, callback)
            }
            is List<*> -> {
                val files: List<File> = value as List<File>
                for (file in files) {
                    fileToBody(builder, key, file, callback)
                }
            }
            else -> {
                builder.addFormDataPart(key, value.toString())
            }
        }
    }
    return builder.build()
}

/**
 * 创建请求体，针对Json请求
 */
fun requestBody(params: MutableMap<String, Any>): RequestBody {
    var content = RestCreator.gson.toJson(params)
    // 这里是Base64编码
    if (RestConfig.isBase64) {
        content = Base64.encode(content.toByteArray())
    }
    return RequestBody.create(MediaType.get("application/json;charset=UTF-8"), content)
    // return content.toRequestBody("application/json;charset=UTF-8".toMediaType())
}

/**
 * 创建文件体
 */
private fun fileToBody(
    builder: MultipartBody.Builder,
    key: String,
    file: File,
    onCallback: ICallback
) {
//    val fileBody: RequestBody = file.asRequestBody(guessFileType(file.absolutePath))
    val fileBody: RequestBody = RequestBody.create(guessFileType(file.absolutePath), file)
    builder.addFormDataPart(key, file.name, RestMultipartBody(fileBody, onCallback))
}

/**
 * 猜测文件类型
 */
private fun guessFileType(path: String): MediaType {
    var contentType = URLConnection.getFileNameMap().getContentTypeFor(path)
    if (contentType == null) {
        contentType = "application/octet-stream"
    }
    return MediaType.get(contentType)
}