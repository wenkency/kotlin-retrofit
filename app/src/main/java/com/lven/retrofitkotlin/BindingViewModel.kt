package com.lven.retrofitkotlin

import android.content.Intent
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.lven.retrofitkotlin.bean.LoginData
import com.lven.retrofitkotlin.bean.LoginResponse
import com.lven.retrofitkotlin.viewmodel.NetViewModel
import com.retrofit.RetrofitPresenter
import com.retrofit.callback.BeanCallback
import com.retrofit.config.CancelNetUtils
import com.retrofit.core.RestClient

/**
 * 销毁自动取消网络请求
 */
class BindingViewModel : NetViewModel() {


    // 绑定TextView
    var name = MutableLiveData("")

    // 绑定了按钮的点击事件
    fun click(view: View) {
        name.value = view.toString()
        var context = view.context
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
    }

    // 请求网络
    fun requestNet() {
        // 这个要
        RetrofitPresenter.post(this, "https://www.wanandroid.com/user/login",
            LoginData("Derry", "1234"),
            object : BeanCallback<LoginResponse>() {
                override fun onSucceed(data: LoginResponse, client: RestClient) {
                    name.value = data.toString()
                }
            })
    }

    /**
     * 销毁自动取消网络请求
     */
    override fun onCleared() {
        // 取消网络请求
        CancelNetUtils.cancel(this)
    }
}