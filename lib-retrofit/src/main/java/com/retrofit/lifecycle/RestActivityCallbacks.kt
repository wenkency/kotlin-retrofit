package com.retrofit.lifecycle

import android.app.Activity
import com.retrofit.cancel.ApiCancelUtils
import com.retrofit.cancel.SuspendCancelUtils

/**
 * 用于Activity销毁时，网络自动取消
 */
class RestActivityCallbacks : EmptyActivityCallbacks() {

    override fun onActivityDestroyed(activity: Activity) {
        // 取消在请求的网络
        ApiCancelUtils.cancel(activity)

        // 协程自动取消
        SuspendCancelUtils.cancel(activity)
    }
}