package com.lven.retrofitkotlin

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.retrofit.RetrofitPresenter
import com.retrofit.callback.BeanCallback
import com.retrofit.callback.CallbackAdapter
import com.retrofit.callback.IObjectCallback
import com.retrofit.core.RestClient
import com.retrofit.core.RestCreator
import com.retrofit.utils.createFile
import com.lven.retrofitkotlin.presenter.MainPresenter
import kotlinx.coroutines.*
import java.io.File

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment(), IObjectCallback {
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
            withContext()
            //postObj()
            //download()
        }
        var image = createFile("image.png")
        if (image.exists() && image.isFile && image.length() > 0) {
            btn.text = "${image.absolutePath}:${image.length()}"
        }

    }

    private fun download() {
        XXPermissions.with(activity).permission(Permission.MANAGE_EXTERNAL_STORAGE)
            .request { _, all ->
                if (all) {
                    doDownload()
                }
            }
    }

    private fun doDownload() {
        val url =
            "https://img.car-house.cn/Upload/activity/20200424/J3GEiBhpAMfkesHCm7EWaQGwxDDwNbMc.png"
        RetrofitPresenter.download(
            activity,
            url,
            activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "image.png",
            object : CallbackAdapter() {
                // 进度
                override fun onProgress(progress: Float, current: Float, total: Float) {
                    btn.text = "$progress:$current:$total"
                }

                // 成功
                override fun onSuccess(file: File) {
                    Log.e("TAG", "onSuccess:" + file.absolutePath)
                    btn.text = "onSuccess:" + file.absolutePath
                }
            })
    }

    private fun get() {
        MainPresenter.get(activity, object : BeanCallback<String>() {
            override fun onSucceed(result: String) {
                btn.text = result
            }
        })
    }

    private fun post() {
        MainPresenter.post(activity, object : BeanCallback<String>() {
            override fun onSucceed(result: String) {
                btn.text = result
            }
        })
    }


    private fun postObj() {
        MainPresenter.postObj(activity, this, String::class.java)
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
        // 总是
        // 6 1 2 3 4 5
    }

    // IObjectCallback ------
    override fun onBefore(client: RestClient, clazz: Class<*>?, requestCode: Int) {
        Log.e("TAG", "onBefore:")
    }

    override fun onAfter(clazz: Class<*>?, requestCode: Int) {
        Log.e("TAG", "onAfter:")
    }

    override fun onSuccess(json: String, data: Any?, clazz: Class<*>?, requestCode: Int) {
        Log.e("TAG", "onSuccess:")
        if (clazz == String::class.java) {
            Log.e("TAG", "onSuccess:true")
        }
        btn.text = json
    }

    override fun onError(code: Int, message: String, clazz: Class<*>?, requestCode: Int) {
        Log.e("TAG", "onError:$message")
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
    // IObjectCallback ------
}