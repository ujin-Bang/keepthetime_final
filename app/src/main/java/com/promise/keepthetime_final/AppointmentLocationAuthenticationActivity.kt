package com.promise.keepthetime_final

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.promise.keepthetime_final.databinding.ActivityAppointmentLocationAuthenticationBinding

class AppointmentLocationAuthenticationActivity : BaseActivity() {
    lateinit var binding: ActivityAppointmentLocationAuthenticationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_appointment_location_authentication)
    }

    override fun setupEvents() {

    }

    override fun setValues() {

    }
}