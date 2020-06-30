package com.lven.retrofit.core

import com.lven.retrofit.callback.ICallback
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.Buffer
import okio.BufferedSink
import okio.ForwardingSink
import okio.Okio
import java.io.IOException

/**
 * MultipartBody用于上传，下载监听进度
 */
class RestMultipartBody(private val requestBody: RequestBody, private val callback: ICallback) :
    RequestBody() {

    private var mCurrentLength = 0f

    override fun contentType(): MediaType? {
        return requestBody.contentType()
    }

    @Throws(IOException::class)
    override fun contentLength(): Long {
        return requestBody.contentLength()
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink?) {
        val totalLength = contentLength().toFloat()
        // 用OKIO的代理类
        val forwardingSink: ForwardingSink = object : ForwardingSink(sink) {
            @Throws(IOException::class)
            override fun write(source: Buffer, byteCount: Long) {
                super.write(source, byteCount)
                mCurrentLength += byteCount.toFloat()
                // 上传进度回调
                val progress = mCurrentLength * 1.0f / totalLength
                // 在
                callback.onProgress(progress, mCurrentLength, totalLength)
            }
        }
        val buffer = Okio.buffer(forwardingSink)
        requestBody.writeTo(buffer)
        buffer.flush()
    }
}