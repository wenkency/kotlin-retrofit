package com.lven.retrofitkotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.lven.retrofit.RxRetrofitPresenter
import com.lven.retrofit.callback.BeanCallback

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
    }

    private fun requestNet() {
        RxRetrofitPresenter.post(activity, "post", Bean("100"),
            object : BeanCallback<String>() {

                override fun onError(code: Int, message: String) {
                    println(message)
                }

                override fun onSucceed(result: String) {
                    btn.text = result
                }
            })
    }
}