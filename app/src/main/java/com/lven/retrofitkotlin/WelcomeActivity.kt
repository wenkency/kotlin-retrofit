package com.lven.retrofitkotlin

import android.content.Intent
import android.os.Bundle
import android.util.ArrayMap
import android.util.Base64

import android.util.Log
import androidx.databinding.DataBindingUtil
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.lven.retrofitkotlin.bean.LoginData
import com.lven.retrofitkotlin.databinding.ActivityWelcomeBinding
import com.lven.retrofitkotlin.utils.AlbumUtils
import com.lven.retrofitkotlin.utils.GlobalUtils
import com.retrofit.RetrofitPresenter
import com.retrofit.RxPresenter
import com.retrofit.callback.BeanCallback
import com.retrofit.core.RestClient
import kotlinx.coroutines.*

import java.io.FileInputStream
import java.util.concurrent.CountDownLatch
import kotlin.concurrent.thread

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
                        //Log.e("TAG", "$data")
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

        // 文件上传
        fun upload() {
            XXPermissions
                .with(this@WelcomeActivity)
                .permission(Permission.WRITE_EXTERNAL_STORAGE)
                .request { _, _ -> AlbumUtils.selectImage(this@WelcomeActivity) }

        }

        fun test() {
            val countDown = CountDownLatch(4)
            val temp = System.currentTimeMillis()
            GlobalUtils.run {
                delay(10)
                Log.e("TAG", "task 1 ${Thread.currentThread().name}")
                countDown.countDown()
            }
            GlobalUtils.run {
                delay(20)
                Log.e("TAG", "task 2 ${Thread.currentThread().name}")
                countDown.countDown()
            }
            GlobalUtils.run {
                delay(30)
                Log.e("TAG", "task 3 ${Thread.currentThread().name}")
                countDown.countDown()
            }
            GlobalUtils.run {
                Log.e("TAG", "task 4 ${Thread.currentThread().name}")
                countDown.countDown()
            }
            countDown.await()

            Log.e("TAG", "test end ${System.currentTimeMillis() - temp}")
        }
    }

    private fun upload(imageUrl: String) {

        val url = "https://liancheng.sanbeikj.cn/mytio/sys/uploadSign"
        val fileInput = FileInputStream(imageUrl)
        var readBytes = fileInput.readBytes()
        val params: MutableMap<String, Any> = ArrayMap()
        params["p_is_android"] = "1"
        params["data"] = Base64.encode(readBytes, Base64.DEFAULT)

        RetrofitPresenter.postForm(this, url, params, object : BeanCallback<String>() {
            override fun onSucceed(data: String, client: RestClient) {
                Log.e("TAG", data)
            }

            override fun onError(code: Int, message: String, client: RestClient) {
                Log.e("TAG", message)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val imageUrl = AlbumUtils.onResult(this, requestCode, resultCode, data)
        if (imageUrl.isNullOrEmpty()) {
            return
        }
        thread {
            upload(imageUrl)
        }

    }
}