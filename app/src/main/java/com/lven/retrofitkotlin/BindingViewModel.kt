package com.lven.retrofitkotlin

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.lven.retrofitkotlin.bean.LoginData
import com.lven.retrofitkotlin.bean.LoginResponse
import com.lven.retrofitkotlin.viewmodel.NetViewModel
import com.retrofit.RetrofitPresenter
import com.retrofit.RxRetrofitPresenter
import com.retrofit.callback.BeanCallback
import com.retrofit.config.CancelNetUtils
import com.retrofit.core.RestClient
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers

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
        // "https://www.wanandroid.com/user/login"
        /*RetrofitPresenter.post(this, "post",
            LoginData("Derry", "1234"),
            object : BeanCallback<LoginResponse>() {
                override fun onSucceed(data: LoginResponse, client: RestClient) {
                    name.value = data.toString()
                }
            })*/
        RxRetrofitPresenter.post(this, "post",
            LoginData("Derry", "1234"),
            object : BeanCallback<String>() {
            override fun onSucceed(data: String, client: RestClient) {
                name.value = data
            }
        })
        /*var service = RxRetrofitPresenter.getRxService()

        val get = service.get("get", mutableMapOf(), mutableMapOf(), "${this.hashCode()}")

        val post = service.postForm("post", mutableMapOf(), mutableMapOf(), "${this.hashCode()}")
        *//*var fc=object : Function<in Integer, out SingleSource<String>>(){}
        }*//*
        get.flatMap { t ->
            var result = t.string()
            Log.e("TAG", "get : $result")
            post
        }.map {
            it.string()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Consumer {
                var result = it
                name.value = result
            })*/
    }

    /**
     * 销毁自动取消网络请求
     */
    override fun onCleared() {
        // 取消网络请求
        CancelNetUtils.cancel(this)
    }
}