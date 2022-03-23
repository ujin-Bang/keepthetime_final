package com.example.keepthetime_final

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.keepthetime_final.databinding.ActivityEditAppointmentBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.overlay.Marker
import java.text.SimpleDateFormat
import java.util.*

class EditAppointmentActivity : BaseActivity() {

    lateinit var binding: ActivityEditAppointmentBinding

    val mSelectedAppointmentDateTime = Calendar.getInstance()

//    약속 장소 관련 멤버변수
    var marker : Marker? = null //지도에 표시될 하나의 마커. 처음에는 찍지 않은 상태

    var mSelectedLatLng: LatLng? = null //약속 장소 위/경도도 처음에는 설정하지 않은 상태
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_edit_appointment)
        setupEvents()
        setValues()
    }



    override fun setupEvents() {

        binding.txtDate.setOnClickListener {

            val dsl = object : DatePickerDialog.OnDateSetListener{
                override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {

                    mSelectedAppointmentDateTime.set(year, month, day)

                    val sdf = SimpleDateFormat("yy/MM/dd")

                    binding.txtDate.text = sdf.format(mSelectedAppointmentDateTime.time)
                }

            }
            val dpd = DatePickerDialog(
                mContext,
                dsl,
                mSelectedAppointmentDateTime.get(Calendar.YEAR),
                mSelectedAppointmentDateTime.get(Calendar.MONTH),
                mSelectedAppointmentDateTime.get(Calendar.DAY_OF_MONTH)

            ).show()
        }

        binding.txtTime.setOnClickListener {
            val tsl = object : TimePickerDialog.OnTimeSetListener{
                override fun onTimeSet(p0: TimePicker?, hourOfDay: Int, minute: Int) {

                    mSelectedAppointmentDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    mSelectedAppointmentDateTime.set(Calendar.MINUTE, minute)

                    val sdf = SimpleDateFormat("a h시 m분")
                    binding.txtTime.text = sdf.format(mSelectedAppointmentDateTime.time)

                }

            }
            val tpd = TimePickerDialog(
                mContext,
                tsl,
                18,
                0,
                false
            ).show()
        }

    }

    override fun setValues() {
//        네이버지도 객체 얻어오기 => 얻어와지며 할 일(Interface)코딩
        binding.naverMapView.getMapAsync {
//            지도로딩이 끝나고 얻어낸 지도객체
            val naverMap = it

//              지도 시작 지점: 덕양구청 위/경도
            val coord = LatLng(37.63774702756897, 126.8322707216135)

//            coord에 설정한 좌표로 > 네이버지도의 카메라 이동
            val cameraUpdate = CameraUpdate.scrollTo(coord)
            naverMap.moveCamera( cameraUpdate)

//            첫 마커 좌표 > 덕양구청
//            val marker = Marker() => 멤버변수로 하나의 마커만 만들어서 관리하자.
            marker = Marker()
            marker!!.position = coord
            marker!!.map = naverMap

//            지도 클릭 이벤트
            naverMap.setOnMapClickListener { pointF, latLng ->

//                Log.d("클릭된 위/경도","위도${latLng.latitude},경도${latLng.longitude}")
//                마커를 새로 추가

                    marker!!.position = latLng
                    marker!!.map = naverMap

            }
        }

    }
}