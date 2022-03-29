package com.example.keepthetime_final

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.keepthetime_final.adapters.InvitedListRecyclerAdapter
import com.example.keepthetime_final.databinding.ActivityAppointmentDetailBinding
import com.example.keepthetime_final.datas.AppointmentData
import com.example.keepthetime_final.datas.BasicResponse
import com.example.keepthetime_final.datas.InvitedFriendData
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat


class AppointmentDetailActivity : BaseActivity() {
    lateinit var binding: ActivityAppointmentDetailBinding

    val mInvitedList = ArrayList<InvitedFriendData>()

    lateinit var mInvitedAdapter: InvitedListRecyclerAdapter

    lateinit var mAppointmentData: AppointmentData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_appointment_detail)
        mAppointmentData = intent.getSerializableExtra("appointmentData")as AppointmentData
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

    }

    override fun setValues() {

        getRequesInvitedListFromServer()
        mInvitedAdapter = InvitedListRecyclerAdapter(mContext, mInvitedList)
        binding.appointmentDetailRecyclerView.adapter = mInvitedAdapter
        binding.appointmentDetailRecyclerView.layoutManager = LinearLayoutManager(mContext)

        txtTitle.text = "약속목록 상세화면"

        val txtInviderFriend = findViewById<TextView>(R.id.txtInviderFriend)
        val txtPlaceName = findViewById<TextView>(R.id.txtPlaceName)
        val txtDateTime = findViewById<TextView>(R.id.txtDateTime)
        val btnRefresh = findViewById<LinearLayout>(R.id.btnRefresh)

        val sdf = SimpleDateFormat("M/d a h:mm")


        txtInviderFriend.text = mAppointmentData.invited_friends.size.toString()
        txtPlaceName.text = mAppointmentData.place
        txtDateTime.text = sdf.format(mAppointmentData.datetime)
        btnRefresh.setOnClickListener {
            finish() //인텐트 종료

            overridePendingTransition(0, 0) //인텐트 효과 없애기

            val intent = intent //인텐트

            startActivity(intent) //액티비티 열기

            overridePendingTransition(0, 0) //인텐트 효과 없애기

        }


        binding.appointmentDetailMapView.getMapAsync {

            val naverMap = it

            val destLatLng = LatLng(mAppointmentData.latitude, mAppointmentData.longitude)

            val startLatLng = LatLng(mAppointmentData.start_latitude, mAppointmentData.start_longitude)

//            val cameraUpdate = CameraUpdate.scrollTo(destLatLng)
//            naverMap.moveCamera(cameraUpdate)

            val cameraPosition = CameraPosition(LatLng(mAppointmentData.latitude, mAppointmentData.longitude), 9.0)
            naverMap.cameraPosition =cameraPosition


           val marker = Marker()
            marker.position = destLatLng
            marker.map = naverMap

            val newMarker = Marker()
            newMarker.position = startLatLng
            newMarker.map = naverMap
            marker.icon = OverlayImage.fromResource(R.drawable.new_map_marker_icon)

            val infoWindow = InfoWindow()
            infoWindow.adapter = object : InfoWindow.DefaultTextAdapter(mContext) {
                override fun getText(infoWindow: InfoWindow): CharSequence {
                    return "출발"
                }
            }

                infoWindow.open(marker)


            val newInfoWindow = InfoWindow()
            newInfoWindow.adapter = object : InfoWindow.DefaultTextAdapter(mContext) {
                override fun getText(infoWindow: InfoWindow): CharSequence {
                    return "도착"
                }
            }

            newInfoWindow.open(newMarker)




        }

    }

    fun getRequesInvitedListFromServer(){
        apilist.getRequestAppointmentDetail(mAppointmentData.id).enqueue(object :Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {

                if (response.isSuccessful){
                    val br = response.body()!!
                    mInvitedList.clear()
                    mInvitedList.addAll(br.data.appointment.invited_friends)
                    mInvitedAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

        })
    }

}