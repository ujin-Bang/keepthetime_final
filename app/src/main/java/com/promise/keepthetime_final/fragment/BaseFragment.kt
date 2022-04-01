package com.promise.keepthetime_final.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.promise.keepthetime_final.api.APIList
import com.promise.keepthetime_final.api.ServerAPI
import com.google.firebase.database.FirebaseDatabase

abstract class BaseFragment : Fragment(){

    lateinit var mContext: Context

    lateinit var apiList: APIList

//    멤버변수로 Realtime에 연결
    val realtimeDB = FirebaseDatabase.getInstance("https://realtimekeepthtimefinal-default-rtdb.asia-southeast1.firebasedatabase.app/")


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mContext = requireContext()

        val retrofit = ServerAPI.getRetrofit(mContext)
        apiList = retrofit.create(APIList::class.java)
    }

    abstract fun setupEvents()
    abstract fun setValues()

}