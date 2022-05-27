package com.retrofit

object RxRetrofitPresenter : IRetrofit {
    override fun isRxService(): Boolean {
        return true
    }
}

