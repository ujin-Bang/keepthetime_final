package com.example.keepthetime_final.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.keepthetime_final.EditAppointmentActivity
import com.example.keepthetime_final.R
import com.example.keepthetime_final.adapters.AppointmentListRecyclerAdapter
import com.example.keepthetime_final.databinding.FragmentAppointmentListBinding
import com.example.keepthetime_final.datas.AppointmentData
import com.example.keepthetime_final.datas.BasicResponse
import com.example.keepthetime_final.datas.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppointmentListFragment: BaseFragment() {
    lateinit var binding: FragmentAppointmentListBinding

    val mAppointmentList = ArrayList<AppointmentData>()

    lateinit var mAdapter: AppointmentListRecyclerAdapter

    companion object{
        lateinit var frag : AppointmentListFragment
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_appointment_list, container, false)
        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupEvents()
        setValues()
        frag = this
    }

    override fun setupEvents() {



    }

    override fun setValues() {

        mAdapter = AppointmentListRecyclerAdapter(mContext,mAppointmentList)
        binding.appointmentRecyclerView.adapter = mAdapter
        binding.appointmentRecyclerView.layoutManager = LinearLayoutManager(mContext)
    }

    override fun onResume() {
        super.onResume()
        getRequestAppointmentListFromServer()
    }

    fun getRequestAppointmentListFromServer(){
        apiList.getRequestAppointmentList().enqueue(object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {

                if (response.isSuccessful){
                    mAppointmentList.clear()
                    val br = response.body()!!

                    mAppointmentList.addAll(br.data.appointments)

                    mAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

        })
    }


}