package com.retrofit.cancel

import com.retrofit.core.RestCreator
import com.retrofit.tag

/**
 * 普通请求，网络取消工具类
 */
object ApiCancelUtils {
    /**
     * 根据Activity取消网络
     */
    fun cancel(any: Any) {
        // 取消正在请求的网络，一般够用
        val runningCalls = RestCreator.httpClient.dispatcher.runningCalls()
        if (runningCalls.isNotEmpty()) {
            for (call in runningCalls) {
                any.tag()?.let {
                    if (it == call.request().tag(String::class.java)) {
                        // 取消网络
                        call.cancel()
                        // 取消Rx的订阅流程
                        RxCancelUtils.cancel(it)
                    }
                }
            }
        }
    }
}