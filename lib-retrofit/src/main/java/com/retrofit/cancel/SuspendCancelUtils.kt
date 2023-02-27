package com.retrofit.cancel

import androidx.collection.ArrayMap
import com.retrofit.tag
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

/**
 * 协程取消操作
 */
object SuspendCancelUtils {

    val map = ArrayMap<Any?, CoroutineScope>(4)

    /**
     * 获取
     */
    fun get(tag: Any?): CoroutineScope {
        val key = tag?.tag()
        var scope = map[key]
        if (scope == null) {
            scope = MainScope()
            map[key] = scope
        }
        return scope
    }

    /**
     * 取消
     */
    fun cancel(tag: Any?) {
        val scope = map.remove(tag?.tag())
        scope?.cancel()
    }

    /**
     * 清空
     */
    fun clear() {
        map.entries.forEach {
            it.value?.cancel()
        }
        map.clear()
    }
}