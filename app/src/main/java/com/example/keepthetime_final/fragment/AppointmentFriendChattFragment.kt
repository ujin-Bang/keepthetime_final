package com.example.keepthetime_final.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.keepthetime_final.R
import com.example.keepthetime_final.databinding.FragmentAppointmentChattBinding
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class AppointmentFriendChattFragment: BaseFragment() {

    lateinit var binding:FragmentAppointmentChattBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = DataBindingUtil.inflate(inflater,R.layout.fragment_appointment_chatt,container,false)
        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {
        binding.btnSend.setOnClickListener {
            val inputContent = binding.edtContent.text.toString()

//            임시: DB의 하위 항목으로 => message 항목 => 0번 항목의 => content항목: 입력내용
            realtimeDB.getReference("message").child("0").child("content").setValue(inputContent)
//            추가 기록: 현재 시간값을 "2022년 3월 5일 오후 5:05" 양식으로 기록

            val now = Calendar.getInstance()
            val sdf = SimpleDateFormat("yyyy년 M월 d일 a h:mm")
            val nowStr = sdf.format(now.time)

            realtimeDB.getReference("message").child("0").child("createdAt").setValue(nowStr)
        }

    }

    override fun setValues() {



    }


}