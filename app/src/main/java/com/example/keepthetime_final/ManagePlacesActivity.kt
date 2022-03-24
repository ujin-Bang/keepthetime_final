package com.example.keepthetime_final

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.keepthetime_final.adapters.StartPlacesListRecyclerAdapter
import com.example.keepthetime_final.databinding.ActivityManagePlacesBinding
import com.example.keepthetime_final.datas.BasicResponse
import com.example.keepthetime_final.datas.PlacesData
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

    }

    override fun setValues() {
        getRequestManagePlacesFromServer()
        mAdapter = StartPlacesListRecyclerAdapter(mContext, mManagePlacesList)
        binding.startPlacesRecyclerView.adapter = mAdapter
        binding.startPlacesRecyclerView.layoutManager = LinearLayoutManager(mContext)
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