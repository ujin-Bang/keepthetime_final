package com.example.keepthetime_final

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.keepthetime_final.api.APIList
import com.example.keepthetime_final.api.ServerAPI

abstract class BaseActivity: AppCompatActivity() {

    lateinit var mContext: Context

    lateinit var apilist: APIList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mContext = this

        val retrofit = ServerAPI.getRetrofit()
        apilist = retrofit.create(APIList::class.java)
    }

    abstract fun setupEvents()
    abstract fun setValues()
}