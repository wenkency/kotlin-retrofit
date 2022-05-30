package com.lven.retrofitkotlin.compress;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 根据图片路径压缩
 */
 class CompressPath extends CompressImage {
    private String imagePath;

    public CompressPath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    protected Bitmap optionImage(Context context, BitmapFactory.Options opts) {
        return BitmapFactory.decodeFile(imagePath, opts);
    }

}
