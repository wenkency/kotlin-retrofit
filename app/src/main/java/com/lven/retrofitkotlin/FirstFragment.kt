package com.lven.retrofitkotlin

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import cn.carhouse.permission.Permission
import cn.carhouse.permission.XPermission
import cn.carhouse.permission.callback.PermissionListenerAdapter
import com.lven.retrofit.RetrofitPresenter
import com.lven.retrofit.api.RestMethod
import com.lven.retrofit.api.RestService
import com.lven.retrofit.callback.BeanCallback
import com.lven.retrofit.callback.ICallback
import com.lven.retrofit.core.RestClient
import com.lven.retrofit.core.RestCreator
import com.lven.retrofit.utils.createFile
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
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
            async()
        }
        var image = createFile("image.png")
        if (image.exists() && image.isFile && image.length() > 0) {
            btn.text = "${image.absolutePath}:${image.length()}"
        }
    }

    private fun download() {
        XPermission.with(activity)
            .permissions(*Permission.STORAGE)
            .request(object :PermissionListenerAdapter(){
            override fun onSucceed() {
                val url = "https://img.car-house.cn/Upload/activity/20200424/J3GEiBhpAMfkesHCm7EWaQGwxDDwNbMc.png"
                RetrofitPresenter.download(activity, url, activity!!.getExternalFilesDir(Environment.DIRECTORY_DCIM), "image.png", object : ICallback {
                    // 进度
                    override fun onProgress(progress: Float, current: Float, total: Float) {
                        btn.text = "$progress:$current:$total"
                    }

                    // 成功
                    override fun onSuccess(file: File) {
                        Log.e("TAG",file.absolutePath)
                    }

                })
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

    fun get() {
        RetrofitPresenter.get(activity, "https://www.baidu.com",
            object : BeanCallback<String>() {
                override fun onSucceed(result: String) {
                    btn.text = result
                }
            })

    }

    /**
     * 同时请求两个接口
     */
    private fun async() {
        val service = RestCreator.getService()
        GlobalScope.launch(Dispatchers.Main) {
            Log.e("TAG", "async---1---")
            val getResult = async(Dispatchers.IO) {
                val get = service.get("get", mutableMapOf(), mutableMapOf(), "${this.hashCode()}")
                val response = get.execute()
                Log.e("TAG", "async---2---")
                response.body()!!.string()
            }
            val postResult = async(Dispatchers.IO) {
                val get =
                    service.postForm("post", mutableMapOf(), mutableMapOf(), "${this.hashCode()}")
                val response = get.execute()
                Log.e("TAG", "async---3---")
                response.body()!!.string()
            }
            Log.e("TAG", "async---4---")

            btn.text = getResult.await() + "\n" + postResult.await()

            Log.e("TAG", "async---5---")
        }
        Log.e("TAG", "async---6---")
        // 6 1 4 3 2 5
        // 6 1 4 2 3 5
    }

    private fun withContext() {
        val service = RestCreator.getService()
        GlobalScope.launch(Dispatchers.Main) {
            Log.e("TAG", "withContext---1---")
            val getResult = withContext(Dispatchers.IO) {
                val get = service.get("get", mutableMapOf(), mutableMapOf(), "${this.hashCode()}")
                val response = get.execute()
                Log.e("TAG", "withContext---2---")
                response.body()!!.string()
            }
            val postResult = withContext(Dispatchers.IO) {
                val get =
                    service.postForm("post", mutableMapOf(), mutableMapOf(), "${this.hashCode()}")
                val response = get.execute()
                Log.e("TAG", "withContext---3---")
                response.body()!!.string()
            }
            Log.e("TAG", "withContext---4---")
            btn.text = getResult + "\n" + postResult
            Log.e("TAG", "withContext---5---")
        }
        Log.e("TAG", "withContext---6---")
    }

}