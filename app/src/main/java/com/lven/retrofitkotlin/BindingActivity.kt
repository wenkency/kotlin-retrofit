package com.lven.retrofitkotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lven.retrofitkotlin.databinding.ActivityBindingBinding

/**
 * MVVM写法，测试网络请求
 */
class BindingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding: ActivityBindingBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_binding)
        binding.vm = getViewModel(BindingViewModel::class.java)
        // LiveData需要感知生命周期
        binding.lifecycleOwner = this
    }


}