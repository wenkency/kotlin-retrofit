package com.lven.retrofitkotlin.compress;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Administrator on 2018/1/10.
 */

public abstract class CompressImage {
    private BitmapFactory.Options opts = new BitmapFactory.Options();

    /**
     * 加载信息
     */
    protected abstract Bitmap optionImage(Context context, BitmapFactory.Options opts);

    /**
     * 压缩图片
     */
    public Bitmap compress(Context context) {
        // 屏幕的宽高
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        return compress(context, screenWidth, screenHeight);
    }

    /**
     * 根据宽高压缩
     */
    public Bitmap compress(Context context, int width, int height) {
        opts.inJustDecodeBounds = true;// 仅仅加载图片的头信息，不是真正加载图片
        // 不是真正加载图片
        optionImage(context, opts);
        // 图片的宽高
        int imgWidth = opts.outWidth;
        int imgHeight = opts.outHeight;
        int scale = 1;// 缩放的宽高
        // 计算绽放比例
        int dx = imgWidth / width;
        int dy = imgHeight / height;
        // 图片比屏幕大，需要缩放
        if (dy > 1 || dx > 1) {
            // 缩放比例取大的
            scale = dx > dy ? dx : dy;
        }
        opts.inSampleSize = scale;// 采样：如果是4  就去加载1/4 * 1/4 也就是原图的1/16的大小到内存
        opts.inJustDecodeBounds = false;// 真正加载图片
        Bitmap bitmap = optionImage(context, opts);
        return bitmap;
    }
}
