package com.example.keepthetime_final.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.keepthetime_final.R
import com.example.keepthetime_final.adapters.ChattingRecyclerAdapter
import com.example.keepthetime_final.databinding.FragmentAppointmentChattBinding
import com.example.keepthetime_final.datas.ChattingData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AppointmentFriendChattFragment: BaseFragment() {

    var messageCount = 0L //DB에 저장된 채팅 갯수 담을 변수. Long타입으로 저장.
    lateinit var binding:FragmentAppointmentChattBinding

    val mChattingList = ArrayList<ChattingData>()

    lateinit var mAdapter : ChattingRecyclerAdapter

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

//                파이어베이스의 DB내용 변경 => 이 함수 실행시켜줌

//                snapshot변수: 현재 변경된 상태 => 자녀가 몇개인지 추출

                messageCount = snapshot.childrenCount

//                snapshot => 마지막 자녀 추출 => ChattingData로 변환 + 목록에 추가.
                mChattingList.add(
                    ChattingData(
                    snapshot.children.last().child("content").value.toString(),
                    snapshot.children.last().child("createdAt").value.toString()
                ) )

                mAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })
        binding.btnSend.setOnClickListener {
            val inputContent = binding.edtContent.text.toString()

            val now = Calendar.getInstance()
            val sdf = SimpleDateFormat("yyyy년 M월 d일 a h:mm")
            val nowStr = sdf.format(now.time)

//          inputContent, nowStr 두개의 데이터를 한번에 묶어서(HashMap) 기록. => onDataChaned함수도 한번만 실행
            val inputMap = hashMapOf< String, String>(
                "content" to inputContent,
                "createdAt" to nowStr
            )
            realtimeDB.getReference("message").child(messageCount.toString()).setValue(inputMap)
        }

    }

    override fun setValues() {

        mAdapter = ChattingRecyclerAdapter(mContext, mChattingList)
        binding.chattingRecyclerView.adapter = mAdapter
        binding.chattingRecyclerView.layoutManager = LinearLayoutManager(mContext)

    }


}