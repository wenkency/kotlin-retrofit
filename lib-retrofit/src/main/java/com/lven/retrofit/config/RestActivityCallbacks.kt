package com.lven.retrofit.config

import android.app.Activity

/**
 * 用于Activity销毁时，网络自动取消
 */
class RestActivityCallbacks : EmptyActivityCallbacks() {

    override fun onActivityDestroyed(activity: Activity) {
        // 取消在请求的网络
        CancelNetUtils.cancel(activity)
    }
}