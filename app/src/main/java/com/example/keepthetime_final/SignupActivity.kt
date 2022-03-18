package com.example.keepthetime_final

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.keepthetime_final.databinding.ActivitySignupBinding
import com.example.keepthetime_final.datas.BasicResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : BaseActivity() {

    lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_signup)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        binding.btnSignup.setOnClickListener {

            val inputEmail = binding.edtEmail.text.toString()
            val inputPw = binding.edtPassword.text.toString()
            val inputNickname = binding.edtNickname.text.toString()

            apilist.putRequestSignup(inputEmail, inputPw, inputNickname).enqueue(object : Callback<BasicResponse>{
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    if( response.isSuccessful ){

                        val br = response.body()!!

                        Toast.makeText(mContext, "${br.data.user.nick_name}님 가입을 축하드립니다.", Toast.LENGTH_SHORT).show()
                    }


                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }

            })

        }

    }

    override fun setValues() {

    }
}