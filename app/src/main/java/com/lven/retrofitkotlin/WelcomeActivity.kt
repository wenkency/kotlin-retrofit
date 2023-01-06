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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityWelcomeBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_welcome)
        binding.lifecycleOwner = this
        // 绑定点击事件
        binding.click = Click()
    }

    inner class Click {
        fun login() {
            // 登录接口
            RxPresenter.postForm(this@WelcomeActivity,
                "/user/login", LoginData("lven", "123456"),
                object : BeanCallback<String>() {
                    override fun onSucceed(data: String, client: RestClient) {
                        Log.e("TAG", "$data")
                    }
                })
        }

        fun unLogin() {
            // 退出登录
            val url = "/user/logout/json"
            RxPresenter.get(this@WelcomeActivity, url, object : BeanCallback<String>() {
                override fun onSucceed(data: String, client: RestClient) {
                    Log.e("TAG", "$data")
                }

            })
        }

        fun netTest() {
            // 收藏接口测试-- cookie
            val url = "/lg/collect/list/0/json"
            RxPresenter.get(this@WelcomeActivity, url,
                object : BeanCallback<String>() {
                    override fun onSucceed(data: String, client: RestClient) {
                        Log.e("TAG", "$data")
                    }

                })
        }
    }
}