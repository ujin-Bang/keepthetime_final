package com.promise.keepthetime_final

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.databinding.DataBindingUtil
import com.promise.keepthetime_final.adapters.AttendanceSpinnerAdapter
import com.promise.keepthetime_final.adapters.StartPlaceSpinnerAdapter
import com.promise.keepthetime_final.databinding.ActivityEditAppointmentBinding
import com.promise.keepthetime_final.datas.BasicResponse
import com.promise.keepthetime_final.datas.PlacesData
import com.promise.keepthetime_final.datas.UserData
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PathOverlay
import com.odsay.odsayandroidsdk.API
import com.odsay.odsayandroidsdk.ODsayData
import com.odsay.odsayandroidsdk.ODsayService
import com.odsay.odsayandroidsdk.OnResultCallbackListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class EditAppointmentActivity : BaseActivity() {

    lateinit var binding: ActivityEditAppointmentBinding

    val mSelectedAppointmentDateTime = Calendar.getInstance()

    //    약속 장소 관련 멤버변수
    var marker: Marker? = null //지도에 표시될 하나의 마커. 처음에는 찍지 않은 상태
    var path: PathOverlay? = null //출발지~ 도착지까지 보여줄 경로선, 처음에는 보이지 않게

    var mSelectedLatLng: LatLng? = null //약속 장소 위/경도도 처음에는 설정하지 않은 상태

    var naverMap: NaverMap? = null

    //    내 출발 장소 목록
    val mStartPlaceList = ArrayList<PlacesData>()
    lateinit var mStartPlaceAdapter: StartPlaceSpinnerAdapter

    // 약속참석인원 리스트/Adapter
    val mAttendanceList = ArrayList<UserData>()
    lateinit var mAttendanceAdapter: AttendanceSpinnerAdapter

    //선택한 약속인원
    var mSelectedAttendance : UserData? = null

    //    선택한 출발 장소
    var mSelectedStartPlace: PlacesData? = null

    // 초대한 인원목록
    val mInviteFriendList = ArrayList<UserData>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_appointment)
        setupEvents()
        setValues()
    }


    override fun setupEvents() {

        binding.btnAddAttendance.setOnClickListener {

            val friendView = LayoutInflater.from(mContext).inflate(R.layout.friend_attendance_spiner_list_item, null)
            binding.friendListLayout.addView(friendView)

            val txtNickname = friendView.findViewById<TextView>(R.id.txtNickname)
            txtNickname.text = mSelectedAttendance!!.nick_name

            mInviteFriendList.add(mSelectedAttendance!!)

            apilist.getRequestMyFriendList("all").enqueue(object : Callback<BasicResponse>{
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    if(response.isSuccessful){
                        val br = response.body()!!
                        mInviteFriendList.addAll(br.data.friends)

                    }
                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }

            })
        }

        binding.attendanceSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {

                        mSelectedAttendance = mAttendanceList[position]



                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }

                    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                    }

                }


//        도전과제 : 스피너의 이벤트 처리
        binding.startPlaceSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {

//                몇번째 아이템이 선택되었는지, p2 or position이 알려줌.

                    mSelectedStartPlace = mStartPlaceList[position]

//                    네이버 지도보다 로딩이 느릴 수 있다.
//                    출발 장소도 로딩이 끝나면 다시 지도 세팅해줌
                    setNaveMap()

//                선택한 출발지 ~ 지도에서 클릭한 도착지까지의 이동 경로 / 교통 정보 표현.
                    findWay()


                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }

//        스크롤 보조용 텍스트뷰에 손이 닿으면 => 스크롤뷰의 이벤트 일시정지(지도만 움직이게)
        binding.txtScrollHelp.setOnTouchListener { view, motionEvent ->

//            스크롤뷰의 이벤트 정지
            binding.scrollView.requestDisallowInterceptTouchEvent(true)

//            텍스트뷰의 터치이벤트만? false => 뒤에 가려져 있는 지도도 터치를 허용해줘야함.
            return@setOnTouchListener false

        }

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
            if (binding.txtTime.text == "약속 시간") {
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

            val selectedStartPlace = mStartPlaceList[binding.startPlaceSpinner.selectedItemPosition]

//            약속일시 - yyyy-MM-dd HH:mm양식을 서버가 지정해서 요청.
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")

            apilist.postRequestAddAppointment(
                inputTitle,
                sdf.format(mSelectedAppointmentDateTime.time),
                selectedStartPlace.name,
                selectedStartPlace.latitude,
                selectedStartPlace.longitude,
                inputPlaceName,
                mSelectedLatLng!!.latitude,
                mSelectedLatLng!!.longitude,
                mSelectedAttendance!!.id.toString(),

                ).enqueue(object : Callback<BasicResponse> {
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    if (response.isSuccessful) {

                        Toast.makeText(mContext, "약속을 등록했습니다", Toast.LENGTH_SHORT).show()
                        finish()
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

        txtTitle.text = "약속 등록하기"
//       네이버지도 객체 얻어오기 => 얻어와지며 할 일(Interface)코딩
        binding.naverMapView.getMapAsync {
//            지도로딩이 끝나고 얻어낸 지도객체
            naverMap = it

            setNaveMap()

        }
//        내 출발장소 목록 불러오기
            getMyStartPlaceListFromServer()

            getAttendanceListFromServer()

//        스피너 어댑터 연결 => 리스트뷰와 동일함
         mAttendanceAdapter = AttendanceSpinnerAdapter(mContext, R.layout.attendance_spinner_list_item, mAttendanceList)
            binding.attendanceSpinner.adapter = mAttendanceAdapter

//        스피너 어댑터 연결 > 리스트뷰와 동일함
            mStartPlaceAdapter = StartPlaceSpinnerAdapter(
                mContext,
                R.layout.start_place_spinner_list_item,
                mStartPlaceList
            )
            binding.startPlaceSpinner.adapter = mStartPlaceAdapter


    }

    fun setNaveMap() {
//        로딩이 끝난 네이버 지도를 가지고 실행할 코드

//        출발지점이 선택 되어야 세팅 진행
        if(mSelectedStartPlace == null) {
            return // 우선 함수 강제 종료
        }
//        네이버 맵도 불러와 져야 세팅 진행
        if(naverMap == null) {
            return //이 상황도 함수 강제 종료
        }

        //              지도 시작 지점: 선택된 출발지점.
        val coord = LatLng(mSelectedStartPlace!!.latitude, mSelectedStartPlace!!.longitude)

//            coord에 설정한 좌표로 > 네이버지도의 카메라 이동
        val cameraUpdate = CameraUpdate.scrollTo(coord)
        naverMap!!.moveCamera(cameraUpdate)

//            첫 마커 좌표 > 덕양구청 ->null
//            val marker = Marker() => 멤버변수로 하나의 마커만 만들어서 관리하자.
//            marker = Marker()
//            marker!!.position = coord
//            marker!!.map = naverMap

////            처음 선택된 좌표 -> 덕양구청위치로
//            mSelectedLatLng = coord

//            지도 클릭 이벤트
        naverMap!!.setOnMapClickListener { pointF, latLng ->

//                Log.d("클릭된 위/경도","위도${latLng.latitude},경도${latLng.longitude}")
//                (찍혀있는 마커가 없다면)마커를 새로 추가
            if (marker == null) {
                marker = Marker()
            }

//                그 마커의 위치 / 지도 적용
            marker!!.position = latLng
            marker!!.map = naverMap
//                약속 장소도 새 좌표로 설정
            mSelectedLatLng = latLng

//                coord~ 선택한 latLng까지 대중 교통 경로를 그려보자(PathOverlay 기능 활용) + ODSay라이브러리 활용


            if (path == null) {
                path = PathOverlay()
            }

//                ArrayList를 만들어서 출발지와 도착지를 추가.
            val coordList = ArrayList<LatLng>()

            coordList.add(coord) //출발지를 임시로 덕양구청으로
            coordList.add(latLng) //지도에 클릭 된 좌표 추가

            path!!.coords = coordList

            path!!.map = naverMap

//            길찾기 API실행
            findWay()

        }
    }

//    길찾기 관련 코드를 별도 함수로 => 여러곳에서 활용 가능
    fun findWay(){

//    출발지 / 도착지 모두 불러와져야 길찾기 진행
    if (mSelectedStartPlace == null || mSelectedLatLng == null){
        return // 좌표가 하나라도 없으면 강제 종료
    }
    val myODsayService =
        ODsayService.init(mContext, "R3sydUQ87JbAGzbchasHghy6awYJaDQnZJg3MdQ1QBE")

    myODsayService.requestSearchPubTransPath(
        mSelectedStartPlace!!.longitude.toString(),
        mSelectedStartPlace!!.latitude.toString(),
        mSelectedLatLng!!.longitude.toString(),
        mSelectedLatLng!!.latitude.toString(),
        null,
        null,
        null,
        object : OnResultCallbackListener {
            override fun onSuccess(p0: ODsayData?, p1: API?) {

                val jsonObj = p0!!.json!!
                Log.d("길찾기 응답", jsonObj.toString())

                val resultObj = jsonObj.getJSONObject("result")
                Log.d("result ", jsonObj.toString())

                val pathArr = resultObj.getJSONArray("path")//여러 추천 경로중 첫번째 만 사용해보자

                val firstPathObj = pathArr.getJSONObject(0) //0번째 경로 추출
                Log.d("첫번째경로 경로", firstPathObj.toString())

//                                    첫번째 경로를 지나는 모든 정거장들의 위경도 값을 담을 목록
                val stationLatLngList = ArrayList<LatLng>()

//                            출발지 좌표를 정거장 목록에 먼저 추가
                stationLatLngList.add(LatLng(mSelectedStartPlace!!.latitude, mSelectedStartPlace!!.longitude))

//                            불광~강남 : 도보 5분/ 지하철 30분 /버스 30분 / 도보 5분
                val subPathArr = firstPathObj.getJSONArray("subPath")

                for (i in 0 until subPathArr.length()) {
                    val subPathObj = subPathArr.getJSONObject(i)
//                                둘러보려는 경로가. 정거장 목록을 내려준다면 (지하철 or 버스)=> 내부파싱
                    if (!subPathObj.isNull("passStopList")) {

                        val passStopListObj = subPathObj.getJSONObject("passStopList")
                        val stationsArr = passStopListObj.getJSONArray("stations")

//                                    실제 정거장 목록 파싱 => 각 정거장의 위도 / 경도 추출 가능 => ArrayList에 담아서 경로선의 좌표로 활용
                        for (j in 0 until stationsArr.length()) {
                            val stationObj = stationsArr.getJSONObject(j)

//                                        위도(y좌표), 경도(x좌표) 추출
                            val lat = stationObj.getString("y").toDouble()
                            val lng = stationObj.getString("x").toDouble()

//                                        네이버 지도의 좌표로 만들어서 > ArrayList에 담자.
                            stationLatLngList.add(LatLng(lat, lng))

                        }

                    }
                }

//                            최종 정거장~ 도착지까지 직선
                stationLatLngList.add(mSelectedLatLng!!)

//                            완성된 정거장 경로들을 =>Path경로로 재설정 지도에 새로 반영

                path!!.coords = stationLatLngList
                path!!.map = naverMap

//                            (첫번째 추천 경로의)정보 항목도 파싱
//                            예상 소요시간 파싱 => 임시로 토스트 출력
                val infoObj = firstPathObj.getJSONObject("info")

                val totalTime = infoObj.getInt("totalTime") //소요분

                val payment = infoObj.getInt("payment")//소요 비용

                val hour = totalTime / 60
                val minutes = totalTime % 60
                val mPayment =
                    NumberFormat.getNumberInstance(Locale.KOREA).format(payment)

                val infoWindow = InfoWindow()
                if (hour == 0) {
                    //                            네이버 지도라이브러리의 InfoWinow 기능 활용

                    infoWindow.adapter =
                        object : InfoWindow.DefaultTextAdapter(mContext) {
                            override fun getText(p0: InfoWindow): CharSequence {


                                return "이동시간: ${minutes}분, 비용: ${mPayment}원"
                            }

                        }
                } else {
                    infoWindow.adapter =
                        object : InfoWindow.DefaultTextAdapter(mContext) {
                            override fun getText(p0: InfoWindow): CharSequence {


                                return "이동시간: ${hour}시간 ${minutes}분, 비용: ${mPayment}원"
                            }

                        }

                }


                infoWindow.open(marker!!)

                marker!!.setOnClickListener {

                    if (marker!!.infoWindow == null) {
                        infoWindow.open(marker!!)

                    } else {
                        infoWindow.close()
                    }
                    return@setOnClickListener true
                }

                val cameraUpdate = CameraUpdate.scrollTo(mSelectedLatLng!!)
                naverMap!!.moveCamera(cameraUpdate)

            }

            override fun onError(p0: Int, p1: String?, p2: API?) {

            }

        }
    )

}

    fun getAttendanceListFromServer(){
        apilist.getRequestMyFriendList("all").enqueue(object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if (response.isSuccessful){
                    val br = response.body()!!

                    mAttendanceList.clear()
                    mAttendanceList.addAll(br.data.friends)
                    mAttendanceAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

        })
    }


    fun getMyStartPlaceListFromServer() {

        apilist.getRequestStratPlacesList().enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {

                if (response.isSuccessful) {
                    val br = response.body()!!
                    mStartPlaceList.clear()
                    mStartPlaceList.addAll(br.data.places)

                    mStartPlaceAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

        })
    }
}
