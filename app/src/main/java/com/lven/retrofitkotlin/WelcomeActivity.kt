package com.lven.retrofitkotlin

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.lven.retrofitkotlin.bean.LoginData
import com.lven.retrofitkotlin.databinding.ActivityWelcomeBinding
import com.retrofit.RxPresenter
import com.retrofit.callback.BeanCallback
import com.retrofit.core.RestClient

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
        fun netTest() {
            RxPresenter.get(this@WelcomeActivity, "get", LoginData("lven", "pwd"),
                object : BeanCallback<String>() {
                    override fun onSucceed(data: String, client: RestClient) {
                        viewModel.result.value = data
                    }

                })
        }
    }
}