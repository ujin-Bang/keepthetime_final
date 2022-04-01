package com.promise.keepthetime_final

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.promise.keepthetime_final.adapters.MyFriendViewPagerAdapter
import com.promise.keepthetime_final.databinding.ActivityManageMyFriendsBinding

class ManageMyFriendsActivity : BaseActivity() {

    lateinit var binding: ActivityManageMyFriendsBinding

    lateinit var mAdapter : MyFriendViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      binding = DataBindingUtil.setContentView(this, R.layout.activity_manage_my_friends)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {


        binding.btnAddFriend.setOnClickListener {

            val myIntent = Intent(mContext, SearchUserActivity::class.java)
            startActivity(myIntent)
        }

    }

    override fun setValues() {
        txtTitle.text = "내 친구 목록 / 친구 추가 요청 목록"

        mAdapter = MyFriendViewPagerAdapter(supportFragmentManager)
        binding.friendsViewPager.adapter = mAdapter
        binding.friendsTabLayout.setupWithViewPager(binding.friendsViewPager)

    }
}

