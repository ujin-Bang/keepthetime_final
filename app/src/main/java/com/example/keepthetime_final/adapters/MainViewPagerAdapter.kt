package com.example.keepthetime_final.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.keepthetime_final.databinding.FragmentMyProfileBinding
import com.example.keepthetime_final.fragment.AppointmentListFragment
import com.example.keepthetime_final.fragment.MyProfileFragment

class MainViewPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {

    override fun getPageTitle(position: Int): CharSequence? {

        return when(position){
            0-> "내 약속목록 리스트"
            else -> "내 프로필"
        }
    }

    override fun getCount() = 2

    override fun getItem(position: Int): Fragment {

        return when(position){

            0 -> AppointmentListFragment()
            else -> MyProfileFragment()
        }
    }
}