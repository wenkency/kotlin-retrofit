package com.retrofit.callback

import com.retrofit.core.RestClient
import java.io.File

/**
 * 回调适配器
 */
open class CallbackAdapter : ICallback {
    override fun onBefore(client: RestClient) {

    }

    override fun onSuccess(file: File) {

    }

    override fun onSuccess(result: String, client: RestClient) {

    }

    override fun onError(code: Int, message: String, client: RestClient) {

    }

    override fun onAfter() {

    }

    override fun onProgress(progress: Float, current: Float, total: Float) {

    }
}