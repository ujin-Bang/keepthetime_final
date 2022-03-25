package com.example.keepthetime_final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.example.keepthetime_final.adapters.MainViewPager2Adapter
import com.example.keepthetime_final.adapters.MainViewPagerAdapter
import com.example.keepthetime_final.databinding.ActivityMainBinding
import com.example.keepthetime_final.datas.BasicResponse
import com.example.keepthetime_final.utils.ContextUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        btnAdd.setOnClickListener {
            val myIntent = Intent(mContext, EditAppointmentActivity::class.java)
            startActivity(myIntent)
        }

        binding.mainBottomNav.setOnItemSelectedListener {

            when(it.itemId){
                R.id.Appointment -> binding.mainViewPager2.currentItem = 0
                R.id.myProfile -> binding.mainViewPager2.currentItem = 1
                R.id.myNote -> binding.mainViewPager2.currentItem = 2

            }
            return@setOnItemSelectedListener true
        }

        binding.mainViewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                binding.mainBottomNav.selectedItemId = when(position){
                    0 -> R.id.Appointment
                    1 -> R.id.myProfile
                    else -> R.id.myNote
                }
            }

        })

    }

    override fun setValues() {

        btnAdd.visibility = View.VISIBLE

    binding.mainViewPager2.adapter = MainViewPager2Adapter(this)


    }
}