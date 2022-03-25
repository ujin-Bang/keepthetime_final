package com.example.keepthetime_final.fragment

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.keepthetime_final.ManageMyFriendsActivity
import com.example.keepthetime_final.ManagePlacesActivity
import com.example.keepthetime_final.R
import com.example.keepthetime_final.SplashActivity
import com.example.keepthetime_final.databinding.FragmentMyProfileBinding
import com.example.keepthetime_final.datas.BasicResponse
import com.example.keepthetime_final.utils.ContextUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyProfileFragment: BaseFragment() {

    lateinit var binding : FragmentMyProfileBinding
    val REQ_CODE_GALLERY  =  2000

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_profile,container, false)
        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        binding.imgProfile.setOnClickListener {
//            이미지 선택 화면으로 이동 - 안드로이드 제공 화면 활용 : Intent(4)

//            다른화면에서 결과 받아오기 - Intent(3): startActivityForResult

            val myIntent = Intent()
            myIntent.action = Intent.ACTION_PICK // 뭔가 가지러 가는 행동이라고 명시
            myIntent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE //사진을 가지러 간다고 명시.
            startActivityForResult(myIntent, REQ_CODE_GALLERY)
        }

        binding.btnManagePlaces.setOnClickListener {
            val myIntent = Intent(mContext, ManagePlacesActivity::class.java)
            startActivity(myIntent)
        }

        binding.btnManageMyFriends.setOnClickListener {

            val myIntent = Intent(mContext, ManageMyFriendsActivity::class.java)
            startActivity(myIntent)
        }

        binding.txtLogOut.setOnClickListener {

            val alert = AlertDialog.Builder(mContext)
                .setTitle("로그아웃")
                .setMessage("정말 로그아웃 하시겠습니까?")
                .setPositiveButton("예", DialogInterface.OnClickListener { dialogInterface, i ->

                    ContextUtil.setLoginUserToken(mContext,"")
                    val myIntent = Intent(mContext, SplashActivity::class.java)
                    myIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(myIntent)
                })
                .setNegativeButton("아니요", null)
                .show()
        }

    }

    override fun setValues() {

        apiList.getRequestMyInfo().enqueue(object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {

                if (response.isSuccessful){

                    val br = response.body()!!

                    Glide.with(mContext).load(br.data.user.profile_img).into(binding.imgProfile)
                    binding.txtNickname.text = br.data.user.nick_name



                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQ_CODE_GALLERY){
            if (requestCode == Activity.RESULT_OK) {
//                data? 변수가 선택된 사진에 대한 정보를 가지고 있다.
                val selectedImageUri = data?.data!! //선택한 사진에 찾아갈 경로(Uri) 받아내기

//                임시 : 선택한 사진을 이미지뷰에 반영
                Glide.with(mContext).load(selectedImageUri).into(binding.imgProfile)
            }
        }

    }

}