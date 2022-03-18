package com.example.keepthetime_final

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.keepthetime_final.databinding.ActivitySigninBinding
import com.example.keepthetime_final.datas.BasicResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInActivity : BaseActivity() {

    lateinit var binding: ActivitySigninBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signin)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        binding.btnSignup.setOnClickListener {



        }

        binding.btnLogin.setOnClickListener {

            val inputEmail = binding.edtEmail.text.toString()
            val inputPw = binding.edtPassword.text.toString()

            apilist.postRequestLogin(inputEmail, inputPw).enqueue( object : Callback<BasicResponse>{
                override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {


                        Log.d("응답확인", response.toString())

                    if(response.isSuccessful){

                        val br = response.body()!!

                        Toast.makeText(mContext, br.message, Toast.LENGTH_SHORT).show()
                    }
                    else{

                        Toast.makeText(mContext, "로그인 실패입니다.확인해주세요", Toast.LENGTH_SHORT).show()

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