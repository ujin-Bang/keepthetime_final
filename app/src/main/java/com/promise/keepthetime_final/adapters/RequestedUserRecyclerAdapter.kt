package com.promise.keepthetime_final.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.promise.keepthetime_final.R
import com.promise.keepthetime_final.api.APIList
import com.promise.keepthetime_final.api.ServerAPI
import com.promise.keepthetime_final.datas.BasicResponse
import com.promise.keepthetime_final.datas.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RequestedUserRecyclerAdapter(
    val mContext: Context,
    val mList: List<UserData>
    ): RecyclerView.Adapter<RequestedUserRecyclerAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val imgProfile = view.findViewById<ImageView>(R.id.imgProfile)
        val txtNickname = view.findViewById<TextView>(R.id.txtNickname)
        val imgSocialLogo = view.findViewById<ImageView>(R.id.imgSocialLogo)
        val txtEmail = view.findViewById<TextView>(R.id.txtEmail)
        val btnAccept = view.findViewById<Button>(R.id.btnAccept)
        val btnDeny = view.findViewById<Button>(R.id.btnDeny)

        fun bind(data : UserData){

            Glide.with(mContext).load(data.profile_img).into(imgProfile)
            txtNickname.text = data.nick_name

            when(data.provider){
                "default" ->{
                    imgSocialLogo.visibility = View.GONE
                    txtEmail.text = data.email
                }
                "kakao" ->{
                    imgSocialLogo.visibility = View.VISIBLE
                    imgSocialLogo.setImageResource(R.drawable.kakao_logo)
                    txtEmail.text = "카카오 로그인"
                }
                "facebook" ->{
                    imgSocialLogo.visibility = View.VISIBLE
                    imgSocialLogo.setImageResource(R.drawable.facebook_logo)
                    txtEmail.text = "페이스북 로그인"
                }
                "naver" ->{
                    imgSocialLogo.visibility = View.VISIBLE
                    imgSocialLogo.setImageResource(R.drawable.naver_logo)
                }
                else ->{

                }


            }

            val ocl = View.OnClickListener {
                val tagStr = it.tag.toString()
                Log.d("보낼파라미터",tagStr)

                val retrofit = ServerAPI.getRetrofit(mContext)
                val apiList = retrofit.create(APIList::class.java)

                apiList.putRequestAcceptOrDenyFriend(data.id, tagStr).enqueue(object : Callback<BasicResponse>{
                    override fun onResponse(
                        call: Call<BasicResponse>,
                        response: Response<BasicResponse>
                    ) {
                        if (response.isSuccessful){

                        }
                    }

                    override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                    }

                })
            }
            btnAccept.setOnClickListener(ocl)
            btnDeny.setOnClickListener(ocl)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(mContext).inflate(R.layout.request_user_list_item, parent, false)
        return MyViewHolder(view)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val data = mList[position]

        holder.bind(data)

    }

    override fun getItemCount() = mList.size
}