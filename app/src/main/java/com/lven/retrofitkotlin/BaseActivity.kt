package com.lven.retrofitkotlin

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

open class BaseActivity : AppCompatActivity() {

    /**
     * 创建ViewModel
     */
    fun <T : ViewModel> getViewModel(clazz: Class<T>): T {
        return ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[clazz]
    }

    /**
     * 子类用
     */
    open fun <T : ViewModel> getAndroidViewModel(
        clazz: Class<T>
    ): T {
        return ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[clazz]
    }
}