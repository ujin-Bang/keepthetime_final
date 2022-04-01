package com.promise.keepthetime_final.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.promise.keepthetime_final.R
import com.promise.keepthetime_final.adapters.RequestedUserRecyclerAdapter
import com.promise.keepthetime_final.databinding.FragmentRequestedUserBinding
import com.promise.keepthetime_final.datas.BasicResponse
import com.promise.keepthetime_final.datas.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RequestedUserFragment:BaseFragment() {

    lateinit var binding: FragmentRequestedUserBinding

    val mRequestUserList = ArrayList<UserData>()

    lateinit var mAdapter : RequestedUserRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_requested_user, container,false)
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

        getRequestedUserFromServer()
        mAdapter = RequestedUserRecyclerAdapter(mContext, mRequestUserList)
        binding.requestedUserRecylerView.adapter = mAdapter
        binding.requestedUserRecylerView.layoutManager = LinearLayoutManager(mContext)

    }

    fun getRequestedUserFromServer(){
        apiList.getRequestMyFriendList("requested").enqueue(object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {

                if (response.isSuccessful){

                    mRequestUserList.clear()
                    val br = response.body()!!
                    mRequestUserList.addAll(br.data.friends)

                    mAdapter.notifyDataSetChanged()

                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

        })

    }

}