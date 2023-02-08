package com.lven.retrofitkotlin.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object GlobalUtils {
    /**
     * 封装
     */
    fun run(default: CoroutineDispatcher = Dispatchers.Default, callback: suspend () -> Unit) {
        GlobalScope.launch(default) {
            callback()
        }
    }
}