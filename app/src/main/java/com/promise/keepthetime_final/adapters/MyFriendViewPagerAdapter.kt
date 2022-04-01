package com.promise.keepthetime_final.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.promise.keepthetime_final.fragment.MyFriendsFragment
import com.promise.keepthetime_final.fragment.RequestedUserFragment

class MyFriendViewPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> "내 친구 목록"
            else -> "친구추가 요청 목록"
        }
    }

    override fun getCount() = 2

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> MyFriendsFragment()
            else -> RequestedUserFragment()
        }
    }
}