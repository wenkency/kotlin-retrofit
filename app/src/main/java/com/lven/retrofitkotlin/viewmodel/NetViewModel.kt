package com.lven.retrofitkotlin.viewmodel

import androidx.lifecycle.ViewModel
import com.retrofit.config.CancelNetUtils

open class NetViewModel : ViewModel() {

    /**
     * 自动取消网络
     */
    override fun onCleared() {
        CancelNetUtils.cancel(this)
    }
}