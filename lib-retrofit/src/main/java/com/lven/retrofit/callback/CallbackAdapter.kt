package com.lven.retrofit.callback

import com.lven.retrofit.core.RestClient
import java.io.File

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