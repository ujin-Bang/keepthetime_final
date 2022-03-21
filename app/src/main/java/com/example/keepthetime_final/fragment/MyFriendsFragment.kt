package com.example.keepthetime_final.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.keepthetime_final.R
import com.example.keepthetime_final.adapters.MyFriendsRecyclerAdapter
import com.example.keepthetime_final.databinding.FragmentMyFriendsBinding
import com.example.keepthetime_final.datas.BasicResponse
import com.example.keepthetime_final.datas.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyFriendsFragment: BaseFragment() {

    lateinit var binding: FragmentMyFriendsBinding
    val mMyFriendsList= ArrayList<UserData>()

    lateinit var mMyFriendsAdapter: MyFriendsRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_friends, container, false)
        return binding.root

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupEvents()
        setValues()
    }
    override fun setupEvents() {

    }

    override fun setValues() {

        getMyFriendsListFormServer()
        mMyFriendsAdapter = MyFriendsRecyclerAdapter(mContext, mMyFriendsList)
        binding.myFriendsRecyclerView.adapter = mMyFriendsAdapter
        binding.myFriendsRecyclerView.layoutManager = LinearLayoutManager(mContext)
    }

    fun getMyFriendsListFormServer(){
        apiList.getRequestMyFriendList("my").enqueue(object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if (response.isSuccessful){

                    mMyFriendsList.clear()
                    val br = response.body()!!

                    mMyFriendsList.addAll(br.data.friends)

                    mMyFriendsAdapter.notifyDataSetChanged()


                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
            }

        })
    }

}