package com.retrofit.core

import com.retrofit.callback.ICallback
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.Buffer
import okio.BufferedSink
import okio.ForwardingSink
import okio.buffer

/**
 * MultipartBody用于上传，下载监听进度
 */
class RestMultipartBody(
    private val body: RequestBody,
    private val callback: ICallback
) : RequestBody() {

    private var sink: BufferedSink? = null

    override fun contentType(): MediaType? {
        return body.contentType()
    }

    override fun contentLength(): Long {
        return body.contentLength()
    }

    override fun writeTo(sink: BufferedSink) {
        // 用OKIO的代理类
        val buffer = getBufferedSink(sink)
        body.writeTo(buffer)
        buffer.flush()
    }

    private fun getBufferedSink(origin: BufferedSink): BufferedSink {
        if (sink == null) {
            val forwardingSink = object : ForwardingSink(origin) {
                private var current = 0f
                override fun write(source: Buffer, byteCount: Long) {
                    super.write(source, byteCount)

                    current += byteCount.toFloat()
                    // 上传进度回调
                    val progress = current * 1.0f / contentLength().toFloat()
                    // 回调
                    callback.onProgress(progress, current, contentLength().toFloat())
                }
            }
            sink = forwardingSink.buffer()
        }
        return sink!!
    }


}