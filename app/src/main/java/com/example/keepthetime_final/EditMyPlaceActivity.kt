package com.example.keepthetime_final

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.keepthetime_final.databinding.ActivityEditMyPlaceBinding
import com.example.keepthetime_final.datas.BasicResponse
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        binding.btnSave.setOnClickListener {

            val inputPlaceName = binding.edtPlaceName.text.toString()
            val isChecked = binding.isPrimaryCheckBox.isChecked
            if (inputPlaceName.isEmpty()){
                Toast.makeText(mContext, "출발 장소의 이름을 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (mSelectedPoint == null){
                Toast.makeText(mContext, "지도를 클릭해 장소를 선택해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            apilist.postRequestAddMyPlace(
                inputPlaceName,
                mSelectedPoint!!.latitude,
                mSelectedPoint!!.longitude,
                isChecked
            ).enqueue(object : Callback<BasicResponse>{
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    if (response.isSuccessful){
                        Toast.makeText(mContext, "출발 장소가 추가 되었습니다", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }

            })
        }

    }

    override fun setValues() {
        txtTitle.text = "출발 장소 추가하기"

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