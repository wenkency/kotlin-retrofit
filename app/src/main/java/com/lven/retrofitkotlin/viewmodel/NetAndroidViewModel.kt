package com.lven.retrofitkotlin.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.retrofit.cancel.ApiCancelUtils
import com.retrofit.cancel.SuspendCancelUtils

open class NetAndroidViewModel(application: Application) : AndroidViewModel(application) {
    /**
     * 自动取消网络
     */
    override fun onCleared() {
        ApiCancelUtils.cancel(this)
        // 协程手动取消
        SuspendCancelUtils.cancel(this)
    }
}