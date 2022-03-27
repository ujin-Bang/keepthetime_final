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

    }

    override fun setValues() {

//        DB연결 -> 값 기록 연습
        val db = FirebaseDatabase.getInstance("https://realtimekeepthtimefinal-default-rtdb.asia-southeast1.firebasedatabase.app/") // 싱가폴 DB주소 대입

//        DB의 하위 정보(Refernce) 설정
         val testRef = db.getReference("test")

//        test 항목에 "Hello Word!!" 기록
         testRef.setValue("Hello World")

    }


}