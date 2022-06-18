package com.retrofit.core

import android.util.Log
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.Buffer
import okio.BufferedSource
import okio.ForwardingSource
import okio.buffer

/**
 * 进度回调，拦截器可以使用
 */
class RestResponseBody(private val body: ResponseBody) : ResponseBody() {

    private val source = object : ForwardingSource(body.source()) {
        var total = 0L
        override fun read(sink: Buffer, byteCount: Long): Long {
            val read = super.read(sink, byteCount)
            if (read >= 0) {
                total += read
                // 进度
                Log.e("TAG", "${contentLength()}:$total")
            }
            return read
        }
    }

    override fun contentLength(): Long {
        return body.contentLength()
    }

    override fun contentType(): MediaType? {
        return body.contentType()
    }

    override fun source(): BufferedSource {
        return source.buffer()
    }
}