package com.lven.retrofitkotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.lven.retrofit.RetrofitPresenter
import com.lven.retrofit.callback.BeanCallback
import com.lven.retrofit.callback.ICallback
import com.lven.retrofit.utils.createFile
import java.io.File

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {
    lateinit var btn: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn = view.findViewById(R.id.textview_first)
        view.findViewById<Button>(R.id.button_first).setOnClickListener {
            //findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            requestNet()
        }
        var image = createFile("image.png")
        if (image.exists() && image.isFile && image.length() > 0) {
            btn.text = "${image.absolutePath}:${image.length()}"
        }
    }

    private fun requestNet() {
        val url =
            "https://img.car-house.cn/Upload/activity/20200424/J3GEiBhpAMfkesHCm7EWaQGwxDDwNbMc.png"
        RetrofitPresenter.download(activity, url, "image.png", object : ICallback {
            // 进度
            override fun onProgress(progress: Float, current: Float, total: Float) {
                btn.text = "$progress:$current:$total"
            }

            // 成功
            override fun onSuccess(file: File) {

            }
        })
    }

    fun post() {
        RetrofitPresenter.post(activity, "post", Bean("100"),
            object : BeanCallback<String>() {
                override fun onSucceed(result: String) {
                    btn.text = result
                }
            })
    }
}