package com.lven.retrofitkotlin.bind

import android.view.View
import androidx.databinding.BindingAdapter

object ImageAdapter {
    /*@JvmStatic
    @BindingAdapter("imageBitmap", requireAll = false)
    fun bitmap(imageView: ImageView, bitmap: Bitmap? = null) {
        bitmap?.let {
            imageView.setImageBitmap(it)
        }
    }*/
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