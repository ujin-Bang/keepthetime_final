package com.example.keepthetime_final

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.keepthetime_final.databinding.ActivityEditMyPlaceBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage

class EditMyPlaceActivity : BaseActivity() {

    lateinit var binding: ActivityEditMyPlaceBinding
//    선택된 장소 저장 변수 / 마커 표시변수
    var mSelectedPoint : LatLng? = null
    var marker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = DataBindingUtil.setContentView(this,R.layout.activity_edit_my_place)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

    }

    override fun setValues() {

        binding.naverMapView.getMapAsync {
            val naverMap = it

            naverMap.setOnMapClickListener { pointF, latLng ->

                if (mSelectedPoint == null) {
//                    처음으로 지도를 클릭한 상황.
//                    마커를 새로 만들어 주자. => 위치정보는? latLng변수가 대입될 예정. 새로 만들 필요가 없다

                    marker = Marker()
                    marker!!.icon = OverlayImage.fromResource(R.drawable.marker_outside_icon)
                }

                mSelectedPoint = latLng

                marker!!.position = latLng
                marker!!.map = naverMap

            }
        }

    }
}