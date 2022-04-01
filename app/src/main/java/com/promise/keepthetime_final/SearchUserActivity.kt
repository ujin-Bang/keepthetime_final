package com.promise.keepthetime_final

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.promise.keepthetime_final.adapters.SearchUserRecyclerAdapter
import com.promise.keepthetime_final.databinding.ActivitySearchUserBinding
import com.promise.keepthetime_final.datas.BasicResponse
import com.promise.keepthetime_final.datas.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchUserActivity : BaseActivity() {

    lateinit var binding: ActivitySearchUserBinding

    val mSearchUserList = ArrayList<UserData>()

    lateinit var mSearchUserAdapter: SearchUserRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_user)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        binding.btnSearch.setOnClickListener {

            val inputKeyword = binding.edtSearchNickname.text.toString()

            apilist.getRequestSearchUserList( inputKeyword)
                .enqueue(object : Callback<BasicResponse> {
                    override fun onResponse(
                        call: Call<BasicResponse>,
                        response: Response<BasicResponse>
                    ) {

                        if (response.isSuccessful) {

                            mSearchUserList.clear()
                            val br = response.body()!!

                            mSearchUserList.addAll(br.data.users)

                            mSearchUserAdapter.notifyDataSetChanged()


                        }
                    }

                    override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                    }

                })
        }

    }


    override fun setValues() {

        txtTitle.text = "친구 추가하기"


        mSearchUserAdapter = SearchUserRecyclerAdapter(mContext, mSearchUserList)
        binding.searchResultUserRecylerView.adapter = mSearchUserAdapter
        binding.searchResultUserRecylerView.layoutManager = LinearLayoutManager(mContext)
    }


}