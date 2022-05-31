package com.lven.retrofitkotlin.bind

import android.view.View
import androidx.databinding.BindingAdapter

object ImageAdapter {
    @JvmStatic
    @BindingAdapter("visibleGone", requireAll = false)
    fun visibleGone(view: View, show: Boolean) {
        view.visibility = if (show) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}