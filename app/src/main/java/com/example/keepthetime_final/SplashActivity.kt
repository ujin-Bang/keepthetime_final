package com.example.keepthetime_final

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.keepthetime_final.datas.BasicResponse
import com.example.keepthetime_final.utils.ContextUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        var myInfoLoaded = false

        apilist.getRequestMyInfo(ContextUtil.getLoginUerToken(mContext)).enqueue(object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {

                if(response.isSuccessful){
                    myInfoLoaded = true
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

        })

        val myHanler = Handler(Looper.getMainLooper())
        myHanler.postDelayed({

            val myIntent: Intent

            if(myInfoLoaded){

                myIntent = Intent(mContext, MainActivity::class.java)

            }
            else {

                myIntent = Intent(mContext,SignInActivity::class.java)

            }
            startActivity(myIntent)


        }, 2500)

    }
}