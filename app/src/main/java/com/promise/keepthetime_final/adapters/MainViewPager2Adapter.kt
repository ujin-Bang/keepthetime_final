package com.promise.keepthetime_final.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.promise.keepthetime_final.fragment.AppointmentListFragment
import com.promise.keepthetime_final.fragment.AppointmentFriendChattFragment
import com.promise.keepthetime_final.fragment.MyProfileFragment

class MainViewPager2Adapter(fa:FragmentActivity): FragmentStateAdapter(fa) {
    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {

        return when(position){

            0 -> AppointmentListFragment()
            1 -> MyProfileFragment()
            else -> AppointmentFriendChattFragment()
        }
    }


}