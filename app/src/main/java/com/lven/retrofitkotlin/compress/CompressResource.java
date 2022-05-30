package com.lven.retrofitkotlin.compress;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 根据资源压缩
 */
 class CompressResource extends CompressImage {
    private int resId;

    public CompressResource(int resId) {
        this.resId = resId;
    }

    @Override
    protected Bitmap optionImage(Context context, BitmapFactory.Options opts) {
        return BitmapFactory.decodeResource(context.getResources(), resId, opts);
    }
}
