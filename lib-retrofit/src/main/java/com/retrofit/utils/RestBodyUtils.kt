package com.retrofit.utils

import android.text.TextUtils
import com.retrofit.callback.ICallback
import com.retrofit.config.RestConfig
import com.retrofit.core.RestClient
import com.retrofit.core.RestCreator
import com.retrofit.core.RestMultipartBody
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.net.URLConnection

/**
 * 创建请求体，针对下载回调请求
 */
fun multipartBody(params: MutableMap<String, Any>, callback: ICallback): MultipartBody {
    // 多文本二进制表单
    val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
    for ((key, value) in params) {
        if (TextUtils.isEmpty(key)) {
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
                // 这里没有调用加密
                builder.addFormDataPart(key, value.toString())
            }
        }
    }
    return builder.build()
}

/**
 * 创建请求体，针对Json请求
 */
fun requestBody(client: RestClient): RequestBody {
    // 请求数据 ，调用了转换如果需要，可以配置
    val content = client.requestConvert(RestCreator.gson.toJson(client.params))

    return content.toRequestBody("application/json;charset=UTF-8".toMediaType())
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
    // 旧版本
    // val fileBody: RequestBody = RequestBody.create(guessFileType(file.absolutePath), file)
    val fileBody = file.asRequestBody(guessFileType(file.absolutePath))
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
    return contentType.toMediaType()
}