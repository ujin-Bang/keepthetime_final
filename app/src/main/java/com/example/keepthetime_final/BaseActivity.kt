package com.example.keepthetime_final

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.keepthetime_final.api.APIList
import com.example.keepthetime_final.api.ServerAPI

abstract class BaseActivity: AppCompatActivity() {

    lateinit var mContext: Context

    lateinit var apilist: APIList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mContext = this

        val retrofit = ServerAPI.getRetrofit(mContext)
        apilist = retrofit.create(APIList::class.java)

        supportActionBar?.let {
            setCustomActionBar()

        }
    }

    abstract fun setupEvents()
    abstract fun setValues()

    fun setCustomActionBar() {

        val defaultActionBar = supportActionBar!!

        defaultActionBar.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM

        defaultActionBar.setCustomView(R.layout.my_custom_action_bar)

        val toolBar = defaultActionBar.customView.parent as Toolbar
        toolBar.setContentInsetsAbsolute(0,0)

    }
}