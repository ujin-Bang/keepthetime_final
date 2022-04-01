package com.promise.keepthetime_final

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.promise.keepthetime_final.adapters.StartPlacesListRecyclerAdapter
import com.promise.keepthetime_final.databinding.ActivityManagePlacesBinding
import com.promise.keepthetime_final.datas.BasicResponse
import com.promise.keepthetime_final.datas.PlacesData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManagePlacesActivity : BaseActivity() {

    lateinit var binding: ActivityManagePlacesBinding
    val mManagePlacesList = ArrayList<PlacesData>()
    lateinit var mAdapter: StartPlacesListRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = DataBindingUtil.setContentView(this,R.layout.activity_manage_places)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        btnAdd.setOnClickListener {
//            장소 추가 화면 이동

            val myIntent = Intent(mContext, EditMyPlaceActivity::class.java)
            startActivity(myIntent)

        }

    }

    override fun setValues() {

        txtTitle.text = "내 출발 장소 관리"
        btnAdd.visibility = View.VISIBLE//숨겨져 있던 추가 버튼을 보이게
        txtEdtPlace.visibility = View.VISIBLE

        mAdapter = StartPlacesListRecyclerAdapter(mContext, mManagePlacesList)
        binding.startPlacesRecyclerView.adapter = mAdapter
        binding.startPlacesRecyclerView.layoutManager = LinearLayoutManager(mContext)
    }

    override fun onResume() {
        super.onResume()
        getRequestManagePlacesFromServer()
    }

    fun getRequestManagePlacesFromServer(){
        apilist.getRequestStratPlacesList().enqueue(object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if (response.isSuccessful){
                    mManagePlacesList.clear()
                    val br = response.body()!!
                    mManagePlacesList.addAll(br.data.places)

                    mAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

        })
    }
}