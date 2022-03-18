package com.example.keepthetime_final

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
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

        binding.edtEmail.addTextChangedListener {

            binding.txtDupleCheck.text = "중복검사를 해주세요"
        }

        binding.edtNickname.addTextChangedListener {

            binding.txtNicknameDupleCheck.text = "중복검사를 해주세요"
        }

        binding.btnDupleCheck.setOnClickListener {

            val inputEmail = binding.edtEmail.text.toString()

            apilist.getRequestDupleCheck("EMAIL",inputEmail).enqueue(object : Callback<BasicResponse>{
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {

                    if (response.isSuccessful){

                        if(binding.edtEmail.length() == 0){
                            binding.txtDupleCheck.text = "이메일을 입력해주세요"

                        }

                        binding.txtDupleCheck.text = "사용해도 좋은 이메일입니다"
                    }
                    else{

                        binding.txtDupleCheck.text ="다른 아이디를 입력해 주세요"
                    }

                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }

            })


        }

        binding.btnNicknameDupleCheck.setOnClickListener {

            val inputNickname = binding.edtNickname.text.toString()

            apilist.getRequestDupleCheck("NICK_NAME",inputNickname).enqueue(object :Callback<BasicResponse>{
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    if(response.isSuccessful){

                        val br = response.body()!!

                        if(binding.edtNickname.length() == 0){
                            binding.txtNicknameDupleCheck.text = "이메일을 입력해주세요."

                        }

                        else{
                            binding.txtNicknameDupleCheck.text = "사용해도 좋은 닉네임입니다."
                        }
                    }
                    else{
                        binding.txtNicknameDupleCheck.text = "다른 닉네임을 사용해주세요."
                    }
                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }
            })

        }

        binding.btnSignup.setOnClickListener {


            val inputEmail = binding.edtEmail.text.toString()
            val inputPw = binding.edtPassword.text.toString()
            val inputNickname = binding.edtNickname.text.toString()

            if(inputEmail.length.equals(0) ) {

                Toast.makeText(mContext, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show()
            }
             else if(inputPw.equals(0)){
                Toast.makeText(mContext, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()

            }
             else if(inputNickname.equals(0)){
                Toast.makeText(mContext, "닉네임을 입력해주세요", Toast.LENGTH_SHORT).show()

            }

            else{



            apilist.putRequestSignup(inputEmail, inputPw, inputNickname).enqueue(object : Callback<BasicResponse>{
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    if( response.isSuccessful ){

                        val br = response.body()!!

                        Toast.makeText(mContext, "${br.data.user.nick_name}님 가입을 축하드립니다.", Toast.LENGTH_SHORT).show()

                        val myIntent = Intent(mContext, SignInActivity::class.java)
                        startActivity(myIntent)
                    }

                    else{
                        Toast.makeText(mContext, response.body()!!.message, Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }

            })


            }
        }

    }

    override fun setValues() {

    }
}