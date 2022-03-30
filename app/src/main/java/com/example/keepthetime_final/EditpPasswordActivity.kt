package com.example.keepthetime_final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.keepthetime_final.databinding.ActivityEditpPasswordBinding
import com.example.keepthetime_final.datas.BasicResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditpPasswordActivity : BaseActivity() {

    lateinit var binding : ActivityEditpPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = DataBindingUtil.setContentView(this,R.layout.activity_editp_password)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {


        binding.btnSave.setOnClickListener {

            val inputPw = binding.edtPassword.text.toString()
            val inputNewPw = binding.edtNewPassword.text.toString()

            if(inputPw.isEmpty()) {
                Toast.makeText(mContext, "기존 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            if(inputNewPw.isEmpty()) {
                Toast.makeText(mContext, "새 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(inputNewPw.length >=5) {
                binding.imgOk.visibility = View.VISIBLE
            }

            else {
                Toast.makeText(mContext, "5글자 이상 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }



            apilist.patchRequestEditPassword(inputPw, inputNewPw).enqueue(object : Callback<BasicResponse>{
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {

                    if (response.isSuccessful){
                        val br = response.body()!!

                        Toast.makeText(mContext, "비밀번호 변경이 완료되었습니다.\n다시로그인 해주세요", Toast.LENGTH_SHORT).show()
                        val myIntent = Intent(mContext, SignInActivity::class.java)
                        startActivity(myIntent)

                    }
                    else{
                        val br = response.body()!!
                        Toast.makeText(mContext, br.message, Toast.LENGTH_SHORT).show()
                        return
                    }

                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }

            })
        }

    }

    override fun setValues() {
        txtTitle.text = "비밀번호 변경"

    }
}