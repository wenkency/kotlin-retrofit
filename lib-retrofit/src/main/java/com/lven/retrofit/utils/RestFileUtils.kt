package com.lven.retrofit.utils

import android.os.Environment
import com.lven.retrofit.callback.ICallback
import com.lven.retrofit.config.RestConfig
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.Consumer
import java.io.*

/**
 * 将文件写到SD卡
 *
 * @param inputStream         文件流
 * @param dir        文件目录
 * @param name 文件名称
 * @param callback 回调
 * @return
 */
fun writeToDisk(
    inputStream: InputStream,
    dir: File?,
    name: String,
    callback: ICallback,
    total: Float
): File {
    val file: File = createFile(dir, name)
    var bis: BufferedInputStream? = null
    var fos: FileOutputStream? = null
    var bos: BufferedOutputStream? = null
    try {
        bis = BufferedInputStream(inputStream)
        fos = FileOutputStream(file)
        bos = BufferedOutputStream(fos)
        val data = ByteArray(1024 * 4)
        var current: Long = 0
        var count: Int
        while (bis.read(data).also { count = it } != -1) {
            current += count.toLong()
            bos.write(data, 0, count)
            // 回调，在主线程去
            Single.just(current)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer {
                    callback.onProgress(it * 100f / total, it.toFloat(), total)
                })

        }
        bos.flush()
        fos.flush()
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        close(bos)
        close(fos)
        close(bis)
        close(inputStream)
    }
    return file
}

/**
 * 下载文件，默认在：RestConfig.context.filesDir
 */
fun createFile(dir: File?, name: String): File {
    if (dir == null) {
        return File(RestConfig.context.filesDir, name)
    }
    // 创建Dir目录
    dir.mkdirs()
    return File(dir, name)
}

/**
 * 通过这个方法，找到下载的文件
 */
fun createFile(name: String): File {
    return createFile(null, name)
}

// 检查是否SDK准备好
private fun checkSDK(): Boolean {
    return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
}

private fun close(closeable: Closeable?) {
    try {
        closeable?.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
