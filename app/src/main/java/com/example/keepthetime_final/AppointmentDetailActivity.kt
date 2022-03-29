package com.example.keepthetime_final

import android.os.Bundle
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.keepthetime_final.databinding.ActivityAppointmentDetailBinding
import com.example.keepthetime_final.datas.AppointmentData
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import java.text.SimpleDateFormat


class AppointmentDetailActivity : BaseActivity() {
    lateinit var binding: ActivityAppointmentDetailBinding

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

        txtTitle.text = "약속목록 상세화면"

        val txtInviderFriend = findViewById<TextView>(R.id.txtInviderFriend)
        val txtPlaceName = findViewById<TextView>(R.id.txtPlaceName)
        val txtDateTime = findViewById<TextView>(R.id.txtDateTime)

//        val sdf = SimpleDateFormat("y/M a h:mm")
//        sdf.format(mAppointmentData.created_at)

        txtInviderFriend.text = mAppointmentData.friend_list
        txtPlaceName.text = mAppointmentData.place
        txtDateTime.text = mAppointmentData.created_at


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

}