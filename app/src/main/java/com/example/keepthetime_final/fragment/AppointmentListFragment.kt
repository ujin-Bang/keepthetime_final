package com.example.keepthetime_final.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.keepthetime_final.EditAppointmentActivity
import com.example.keepthetime_final.R
import com.example.keepthetime_final.databinding.FragmentAppointmentListBinding

class AppointmentListFragment: BaseFragment() {
    lateinit var binding: FragmentAppointmentListBinding

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
    }

    override fun setupEvents() {

        binding.btnAddAppointment.setOnClickListener {

            val myIntent = Intent(mContext,EditAppointmentActivity::class.java)
            startActivity(myIntent)
        }

    }

    override fun setValues() {

    }


}