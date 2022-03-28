package com.example.keepthetime_final.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.keepthetime_final.R
import com.example.keepthetime_final.databinding.FragmentAppointmentChattBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class AppointmentFriendChattFragment: BaseFragment() {

    var messageCount = 0L //DB에 저장된 채팅 갯수 담을 변수. Long타입으로 저장.
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

//        realtimeDb의 항목중, message 항목에 변화가 생길때
        realtimeDB.getReference("message").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

//                파이어베이스의DB내용 변경 => 이 함수 실행시켜줌

//                snapshot변수: 현재 변경된 상태 => 자녀가 몇개인지 추출

                messageCount = snapshot.childrenCount


            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })
        binding.btnSend.setOnClickListener {
            val inputContent = binding.edtContent.text.toString()

//            임시: DB의 하위 항목으로 => message 항목 => 0번 항목의 => content항목: 입력내용
            realtimeDB.getReference("message").child(messageCount.toString()).child("content").setValue(inputContent)
//            추가 기록: 현재 시간값을 "2022년 3월 5일 오후 5:05" 양식으로 기록

            val now = Calendar.getInstance()
            val sdf = SimpleDateFormat("yyyy년 M월 d일 a h:mm")
            val nowStr = sdf.format(now.time)

            realtimeDB.getReference("message").child(messageCount.toString()).child("createdAt").setValue(nowStr)
        }

    }

    override fun setValues() {



    }


}