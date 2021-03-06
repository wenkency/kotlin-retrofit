package com.lven.retrofit.config

import android.app.Activity
import com.lven.retrofit.core.RestCreator
import com.lven.retrofit.tag

/**
 * 网络取消工具类
 */
object CancelNetUtils {
    /**
     * 根据Activity取消网络
     */
    fun cancel(activity: Activity) {
        // 取消在请求的网络
        var runningCalls = RestCreator.httpClient.dispatcher().runningCalls()
        if (runningCalls.isNotEmpty()) {
            for (call in runningCalls) {
                activity.tag().let {
                    if (it == call.request().tag(String::class.java)) {
                        call.cancel()
                    }
                }
            }
        }
    }
}