package com.promise.keepthetime_final

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.promise.keepthetime_final.databinding.ActivitySigninBinding
import com.promise.keepthetime_final.datas.BasicResponse
import com.promise.keepthetime_final.utils.ContextUtil
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

            val myIntent = Intent( mContext, SignupActivity::class.java)
            startActivity(myIntent)

        }


        binding.btnLogin.setOnClickListener {


            val inputEmail = binding.edtEmail.text.toString()
            val inputPw = binding.edtPassword.text.toString()


            apilist.postRequestLogin(inputEmail, inputPw).enqueue( object : Callback<BasicResponse>{
                override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {


                        Log.d("응답확인", response.toString())

                    if(response.isSuccessful){

                        val br = response.body()!!

                        Toast.makeText(mContext, "${br.data.user.nick_name}님 환영합니다.", Toast.LENGTH_SHORT).show()

//                        로그인하면 토큰값을 내려줌 , 서버가 내려주는 토큰값 저장
                        ContextUtil.setLoginUserToken(mContext, br.data.token)

                        val myItent = Intent(mContext, MainActivity::class.java)
                        startActivity(myItent)

                        finish()
                    }
                    else{

                        Toast.makeText(mContext, "비밀번호 이메일을 다시 확인해주세요", Toast.LENGTH_SHORT).show()


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