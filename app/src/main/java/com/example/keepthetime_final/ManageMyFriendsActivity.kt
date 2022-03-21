package com.example.keepthetime_final

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.keepthetime_final.adapters.MyFriendAdapter
import com.example.keepthetime_final.databinding.ActivityManageMyFriendsBinding
import com.example.keepthetime_final.datas.BasicResponse
import com.example.keepthetime_final.datas.UserData
import com.example.keepthetime_final.utils.ContextUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManageMyFriendsActivity : BaseActivity() {

    lateinit var binding: ActivityManageMyFriendsBinding

    val mMyFriendList = ArrayList<UserData>()

    lateinit var mAdapter: MyFriendAdapter

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

       getMyFriendListFromServer()

        mAdapter = MyFriendAdapter(mContext,R.layout.my_friends_list_item, mMyFriendList)
        binding.MyFriendListView.adapter = mAdapter

    }
    fun getMyFriendListFromServer(){
        apilist.getRequestMyFriendList(ContextUtil.getLoginUerToken(mContext),"all").enqueue(object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {

                if (response.isSuccessful){

                    val br = response.body()!!

                    mMyFriendList.addAll(br.data.friends)

                    mAdapter.notifyDataSetChanged()



                }

            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

        })
    }
}

