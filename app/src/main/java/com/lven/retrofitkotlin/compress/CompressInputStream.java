package com.lven.retrofitkotlin.compress;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * 根据资源压缩
 */
 class CompressInputStream extends CompressImage {
    private InputStream mInputStream;
    private byte[] mArray;

    public CompressInputStream(InputStream inputStream) {
        mInputStream = inputStream;
        try {
            mArray = readStream(mInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Bitmap optionImage(Context context, BitmapFactory.Options opts) {
        return BitmapFactory.decodeByteArray(mArray, 0, mArray.length, opts);
    }

    private byte[] readStream(InputStream inStream) throws Exception {
        byte[] buffer = new byte[1024 * 1024];
        int len = -1;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inStream.close();
        return data;
    }
}
