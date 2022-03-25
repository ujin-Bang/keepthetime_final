package com.example.keepthetime_final

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.keepthetime_final.databinding.ActivityViewMapBinding
import com.example.keepthetime_final.datas.AppointmentData
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PathOverlay
import com.odsay.odsayandroidsdk.API
import com.odsay.odsayandroidsdk.ODsayData
import com.odsay.odsayandroidsdk.ODsayService
import com.odsay.odsayandroidsdk.OnResultCallbackListener
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class ViewMapActivity : BaseActivity() {

    lateinit var binding: ActivityViewMapBinding

    lateinit var mAppointment: AppointmentData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_view_map)
        mAppointment = intent.getSerializableExtra("appointment")as AppointmentData
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

    }

    override fun setValues() {

//      제목 문구를 "약속장소 지도 확인"
        txtTitle.text = "약속장소 지도 확인"

binding.naverMapView.getMapAsync {

            val naverMap = it

//            도착지 자체를 변수화
            val destLatLng = LatLng(mAppointment.latitude, mAppointment.longitude)

//            도착지로 카메라 이동
            val cameraUpdate = CameraUpdate.scrollTo(destLatLng)
            naverMap.moveCamera(cameraUpdate)

//            마커도 찍어주자.
            val marker = Marker()
            marker.position = destLatLng
            marker.map = naverMap

//            Path 객체의 좌표를 설정 =>naverMap에 추가
//             val path = PathOverlay( )

//            path.coords => 출발지/ 도착지만 넣어서 대입하면? 일직선 열결

//            출발지~ 도착지 사이의 정거장이 있다면 정거장들을 좌표로 추가.

            val stationList = ArrayList<LatLng>()

//            첫 좌표는 출발점
            stationList.add(LatLng(mAppointment.start_latitude, mAppointment.start_longitude))

//            거치게 되는 정거장들 목록을 >ODSayService로 받아서 추가

            val myODsayService = ODsayService.init(mContext,"R3sydUQ87JbAGzbchasHghy6awYJaDQnZJg3MdQ1QBE")
            myODsayService.requestSearchPubTransPath(
                mAppointment.start_longitude.toString(),
                mAppointment.start_latitude.toString(),
                mAppointment.longitude.toString(),
                mAppointment.latitude.toString(),
                null,
                null,
                null,
                object : OnResultCallbackListener{
                    override fun onSuccess(p0: ODsayData?, p1: API?) {

//                        JSONObject가 주는 것을 -> jsonObj에 받아서 > 내부 하나씩 파싱
                        val jonObj = p0!!.json
                        Log.d("대중교통 길찾기", jonObj.toString())

                        val resultObj = jonObj.getJSONObject("result")
                        Log.d("리졸트확인", resultObj.toString())

                        val pathArr = resultObj.getJSONArray("path")
                        val firstPathObj = pathArr.getJSONObject(0)

                        Log.d("첫번째패스",firstPathObj.toString())

                        val subPathArr = firstPathObj.getJSONArray("subPath")

                        for(i in 0 until subPathArr.length()){
                            val subPathObj = subPathArr.getJSONObject(i)

                            if(!subPathObj.isNull("passStopList")){
//                                도보가 아니어서 정거장 목록을 주는 경우
                                val passStopListObj = subPathObj.getJSONObject("passStopList")
                                val stationsArr = passStopListObj.getJSONArray("stations")

                                for(j in 0 until stationsArr.length()){
                                    val stationObj = stationsArr.getJSONObject(j)
                                    val stationLat = stationObj.getString("y").toDouble()
                                    val stationLng = stationObj.getString("x").toDouble()

//                                    네이버 지도의 경로선에 그려줄 좌표 목록에 추가
                                    stationList.add(LatLng(stationLat, stationLng))
                                }
                            }
                        }

//                        모든 정거장 목록이 추가되어있다.
//                        마지막 경로선으로, 도착지를 추가.
                        stationList.add(destLatLng)

//                        경로선을 지도에 그려주자
                        val path = PathOverlay()
                        path.coords = stationList
                        path.map = naverMap

//                        파싱을 추가로 하면, 소요시간/ 비용 정보도 얻을 수 있다 => infoWindow에 결합.
                        val infoObj = firstPathObj.getJSONObject("info")
                        val minutes = infoObj.getInt("totalTime")
                        val payment = infoObj.getInt("payment")

                        val hour = minutes/60
                        val reMinutes = minutes%60

                        if(hour == 0){
                            val commaPayment = NumberFormat.getNumberInstance(Locale.KOREA).format(payment)


                            //                        정보창에 띄우기
                            val infoWindow = InfoWindow()
                            infoWindow.adapter = object :InfoWindow.DefaultViewAdapter(mContext){
                                override fun getContentView(p0: InfoWindow): View {
//                                    리턴 자료형: View를 리턴
//                                    View객체를 만드는 방법? => LayoutInflater에게 inflate 시키면 >결과물이 View가 된다.
                                    val view = LayoutInflater.from(mContext).inflate(R.layout.destination_info_window, null)

                                    val txtPlaceName = view.findViewById<TextView>(R.id.txtPlaceName)
                                    val txtMoveTime = view.findViewById<TextView>(R.id.txtMoveTime)
                                    val txtPayment = view.findViewById<TextView>(R.id.txtPayment)

                                    txtPlaceName.text = mAppointment.place
                                    txtMoveTime.text = "이동 시간 : ${reMinutes}분 "
                                    txtPayment.text = "비용 : ${commaPayment}원"

                                    return view

                           }

                            }
                            infoWindow.open(marker)
                        }
                        else {

                            val commaPayment = NumberFormat.getNumberInstance(Locale.KOREA).format(payment)


                            //                        정보창에 띄우기
                            val infoWindow = InfoWindow()
                            infoWindow.adapter = object :InfoWindow.DefaultViewAdapter(mContext){
                                override fun getContentView(p0: InfoWindow): View {
//                                    리턴 자료형: View를 리턴
//                                    View객체를 만드는 방법? => LayoutInflater에게 inflate 시키면 >결과물이 View가 된다.
                                    val view = LayoutInflater.from(mContext).inflate(R.layout.destination_info_window, null)

                                    val txtPlaceName = view.findViewById<TextView>(R.id.txtPlaceName)
                                    val txtMoveTime = view.findViewById<TextView>(R.id.txtMoveTime)
                                    val txtPayment = view.findViewById<TextView>(R.id.txtPayment)

                                    txtPlaceName.text = mAppointment.place
                                    txtMoveTime.text = "이동 시간 : ${hour}시간 ${reMinutes}분 "
                                    txtPayment.text = "비용 : ${commaPayment}원"

                                    return view

                                }

                            }
                            infoWindow.open(marker)

                        }


                    }

                    override fun onError(p0: Int, p1: String?, p2: API?) {

                    }

                }
            )
        }

    }
}