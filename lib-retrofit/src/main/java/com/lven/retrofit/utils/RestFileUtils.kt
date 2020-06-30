package com.lven.retrofit.utils

import android.os.Environment
import com.lven.retrofit.callback.ICallback
import com.lven.retrofit.config.RestConfig
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
    dir: String,
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
            // 回调
            callback.onProgress(current * 100f / total, current.toFloat(), total)
            bos.write(data, 0, count)
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

fun createDir(dirName: String): File {
    var dir = if (checkSDK()) File(dirName) else RestConfig.context.filesDir
    if (!dir.exists()) {
        dir.mkdirs()
    }
    return dir
}

fun createFile(dir: String, name: String): File {
    return File(createDir(dir), name)
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
