package com.retrofit.config

import androidx.collection.ArrayMap
import com.retrofit.tag
import io.reactivex.rxjava3.disposables.Disposable

/**
 * 取消Rx网络请求的订阅
 */
object RxCancelUtils {
    // key:tag  value( key:Disposable.tag value:Disposable)
    private val map = ArrayMap<String, ArrayMap<String, Disposable>>()

    /**
     * 根据TAG，添加Disposable
     */
    fun add(tag: String, disposable: Disposable) {
        var values = map[tag]
        if (values == null) {
            values = ArrayMap()
        }
        // Disposable赋值
        values[disposable.tag()] = disposable
        // 这个是一级
        map[tag] = values
    }

    /**
     * 根据TAG移除Disposable
     */
    fun remove(tag: String, disposable: Disposable) {
        val values = map[tag]
        values?.let {
            // 移除Disposable
            it.remove(disposable.tag())
            // 如果value是空的Map，就移除Map
            if (it.isEmpty) {
                map.remove(tag)
            }
        }
    }

    /**
     * 根据TAG取消订阅
     */
    fun cancel(tag: String) {
        // 拿到TAG对应的所有Disposable
        val values = map.remove(tag)
        values?.let { items ->
            items.forEach { item ->
                if (!item.value.isDisposed) {
                    item.value.dispose()
                }
            }
        }
    }

    /**
     * 清空所有
     */
    fun clear() {
        map.clear()
    }

    fun size(): Int {
        return map.size
    }
}