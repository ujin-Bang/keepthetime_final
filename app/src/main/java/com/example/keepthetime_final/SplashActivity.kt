package com.example.keepthetime_final

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

    }

    override fun setValues() {

        val myHanler = Handler(Looper.getMainLooper())
        myHanler.postDelayed({

                 val myIntent = Intent(mContext, SignInActivity::class.java)
                startActivity(myIntent)

        }, 2500)

    }
}