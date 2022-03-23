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
import com.example.keepthetime_final.datas.BasicResponse
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.overlay.Marker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class EditAppointmentActivity : BaseActivity() {

    lateinit var binding: ActivityEditAppointmentBinding

    val mSelectedAppointmentDateTime = Calendar.getInstance()

    //    약속 장소 관련 멤버변수
    var marker: Marker? = null //지도에 표시될 하나의 마커. 처음에는 찍지 않은 상태

    var mSelectedLatLng: LatLng? = null //약속 장소 위/경도도 처음에는 설정하지 않은 상태


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_appointment)
        setupEvents()
        setValues()
    }


    override fun setupEvents() {

//        저장버튼이 눌리면
        binding.btnAppointmentSave.setOnClickListener {

                val inputTitle = binding.edtAppointmentTitle.text.toString()
            if (inputTitle.isEmpty()) {
                Toast.makeText(mContext, "약속 제목을 입력해 주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (binding.txtDate.text == "약속 일자") {
                Toast.makeText(mContext, "약속일자를 입력해 주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
//            선택일시가 지금보다 이전의 일시라면 "현재 이후의 시간으로 선택해주세요" 토스트 띄우기
            val now = Calendar.getInstance()
            if (mSelectedAppointmentDateTime.timeInMillis < now.timeInMillis) {
                Toast.makeText(mContext, "현재날짜 이후로 입력해 주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (  binding.txtTime.text == "약속 시간") {
            Toast.makeText(mContext, "약속시간을 입력해 주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
        }
            val inputPlaceName = binding.edtPlaceName.text.toString()
            if (inputPlaceName.isEmpty()) {
                Toast.makeText(mContext, "약속 장소를 입력해 주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            //            장소를 선택했는지? 안했다면 등록 거부
            if (mSelectedLatLng == LatLng(37.63774702756897, 126.8322707216135)) {
                Toast.makeText(mContext, "약속 장소를 선택하지 않았습니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Log.d("선택한 약속장소 - 위도", "위도: ${mSelectedLatLng!!.latitude}")
            Log.d("선택한 약속장소 - 경도", "경도: ${mSelectedLatLng!!.longitude}")

//            약속일시 - yyyy-MM-dd HH:mm양식을 서버가 지정해서 요청.
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")

            apilist.postRequestAddAppointment(
                inputTitle,
                sdf.format(mSelectedAppointmentDateTime.time),
                inputPlaceName,
                mSelectedLatLng!!.latitude,
                mSelectedLatLng!!.longitude,

            ).enqueue(object : Callback<BasicResponse>{
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    if(response.isSuccessful){
                        val br = response.body()!!

                        Log.d("약속장소 응답확인-",br.toString())
                    }
                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }

            })


        }

        binding.txtDate.setOnClickListener {

            val dsl = object : DatePickerDialog.OnDateSetListener {
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
            val tsl = object : TimePickerDialog.OnTimeSetListener {
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
            naverMap.moveCamera(cameraUpdate)

//            첫 마커 좌표 > 덕양구청 ->null
//            val marker = Marker() => 멤버변수로 하나의 마커만 만들어서 관리하자.
//            marker = Marker()
//            marker!!.position = coord
//            marker!!.map = naverMap

////            처음 선택된 좌표 -> 덕양구청위치로
//            mSelectedLatLng = coord

//            지도 클릭 이벤트
            naverMap.setOnMapClickListener { pointF, latLng ->

//                Log.d("클릭된 위/경도","위도${latLng.latitude},경도${latLng.longitude}")
//                (찍혀있는 마커가 없다면)마커를 새로 추가
                if(marker == null) {
                    marker = Marker()
                }

//                그 마커의 위치 / 지도 적용
                marker!!.position = latLng
                marker!!.map = naverMap
//                약속 장소도 새 좌표로 설정
                mSelectedLatLng = latLng

            }
        }

    }
}