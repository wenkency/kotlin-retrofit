package com.lven.retrofitkotlin

import android.app.Application
import android.content.Intent
import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.lven.retrofitkotlin.compress.CompressImageFactory
import com.retrofit.RxRetrofitPresenter
import com.retrofit.callback.CallbackAdapter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * 1. 加载网络图片，如果2秒内加载成功，就再显示图片2秒。2秒内加载不成功，到首页
 */
class WelcomeViewModel(application: Application) : AndroidViewModel(application) {
    val app = application

    var bitmap = MutableLiveData<Bitmap>()
    var visible = MutableLiveData(true)

    var isShow = false
    var disposable: Disposable? = null

    // 文件转成bitmap
    fun file2Bitmap(file: File) {
        Observable.just(file)
            .map {
                var factory = CompressImageFactory.create(it.absolutePath)
                factory.compress(app)
            }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                visible.value = false
                bitmap.value = it
                isShow = true
                Observable.timer(2, TimeUnit.SECONDS).subscribe {
                    toMain()
                }
            }
    }

    fun loadImage() {
        // 1. 加载网络图片，如果2秒内加载成功，就再显示图片2秒。2秒内加载不成功，到首页
        val url = "https://alifei01.cfp.cn/creative/vcg/800/new/VCG2183b65c6ce-TJY.jpg"
        var dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        RxRetrofitPresenter.download(this, url, dir, "image.png",
            object : CallbackAdapter() {
                override fun onProgress(progress: Float, current: Float, total: Float) {
                    //Log.e("TAG", "progress:${progress} :${RxCancelUtils.size()}")
                }

                override fun onSuccess(file: File) {
                    // todo 缓存
                    Log.e("TAG", "onSuccess:${file.absoluteFile}")
                    file2Bitmap(file)
                }
            })
        // 每隔500毫秒检查一次
        disposable = Observable.interval(500, TimeUnit.MILLISECONDS)
            .take(4)
            .subscribe {
                if (isShow) {
                    disposable?.dispose()
                } else {
                    if (it == 4L) {
                        toMain()
                    }
                }
            }
    }


    fun toMain() {
        val intent = Intent(app, MainActivity::class.java)
        app.startActivity(intent)
    }
}