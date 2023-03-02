package com.lven.retrofitkotlin.viewmodel

import androidx.lifecycle.ViewModel
import com.retrofit.cancel.ApiCancelUtils
import com.retrofit.cancel.SuspendCancelUtils

open class NetViewModel : ViewModel() {

    /**
     * 自动取消网络
     */
    override fun onCleared() {
        ApiCancelUtils.cancel(this)
        // 协程手动取消
        SuspendCancelUtils.cancel(this)
    }
}