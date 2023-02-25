package com.lven.retrofitkotlin.utils

import kotlinx.coroutines.*

object GlobalUtils : CoroutineScope by MainScope() {
    /**
     * 封装
     */
    fun run(default: CoroutineDispatcher = Dispatchers.Default, callback: suspend () -> Unit) {
        launch(default) {
            callback()
        }
    }
}