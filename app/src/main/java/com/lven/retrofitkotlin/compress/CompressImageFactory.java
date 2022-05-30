package com.lven.retrofitkotlin.compress;

import java.io.InputStream;

/**
 * 压缩的构建类
 */

public class CompressImageFactory {
    public static CompressImage create(InputStream inputStream) {
        return new CompressInputStream(inputStream);
    }

    public static CompressImage create(String imagePath) {
        return new CompressPath(imagePath);
    }

    public static CompressImage create(int resId) {
        return new CompressResource(resId);
    }
}
