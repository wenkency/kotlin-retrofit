package com.lven.retrofitkotlin.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.retrofit.config.CancelNetUtils

open class NetAndroidViewModel(application: Application) : AndroidViewModel(application) {
    /**
     * 自动取消网络
     */
    override fun onCleared() {
        CancelNetUtils.cancel(this)
    }
}