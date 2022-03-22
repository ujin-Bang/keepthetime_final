package com.example.keepthetime_final

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.keepthetime_final.databinding.ActivityEditAppointmentBinding

class EditAppointmentActivity : BaseActivity() {

    lateinit var binding: ActivityEditAppointmentBinding

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

                    Toast.makeText(mContext, "${year}년${month}월${day}일 선택함", Toast.LENGTH_SHORT).show()

                }

            }
            val dpd = DatePickerDialog(
                mContext,
                dsl,
                2022,
                3,
                22
            ).show()
        }

    }

    override fun setValues() {

    }
}