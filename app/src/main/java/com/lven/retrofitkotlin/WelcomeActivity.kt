package com.lven.retrofitkotlin

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.lven.retrofitkotlin.databinding.ActivityWelcomeBinding
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

class WelcomeActivity : BaseActivity() {
    private val viewModel: WelcomeViewModel by lazy {
        getAndroidViewModel(WelcomeViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityWelcomeBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_welcome)
        binding.lifecycleOwner = this
        binding.vm = viewModel
        binding.click = Click()

        // 加载图片显示
        viewModel.loadImage()
    }

    inner class Click {
        fun rxjava() {

            val tem = System.currentTimeMillis()

            Observable.interval(500, TimeUnit.MILLISECONDS)
                .take(4)
                .subscribe {
                    Log.e(
                        "TAG",
                        "${Thread.currentThread().name}:$it:${System.currentTimeMillis() - tem}"
                    )
                }
            //SystemClock.sleep(100)
            //disposable?.dispose()
        }
    }
}