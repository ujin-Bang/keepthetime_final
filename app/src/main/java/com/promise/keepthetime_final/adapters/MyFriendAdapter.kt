package com.promise.keepthetime_final.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.promise.keepthetime_final.R
import com.promise.keepthetime_final.datas.UserData

class MyFriendAdapter(
    val mContext: Context,
    resId: Int,
    val mList: List<UserData>
): ArrayAdapter<UserData>(mContext, resId, mList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var tempRow = convertView
        if(tempRow == null ) {
            tempRow = LayoutInflater.from(mContext).inflate(R.layout.my_friends_list_item, null)
        }

        val row = tempRow!!

        val data = mList[position]

        val imgProfile = row.findViewById<ImageView>(R.id.imgProfile)
        val txtNickname = row.findViewById<TextView>(R.id.txtNickname)
        val txtEmail = row.findViewById<TextView>(R.id.txtEmail)
        val imgSocialLogo = row.findViewById<ImageView>(R.id.imgSocialLogo)

        Glide.with(mContext).load(data.profile_img).into(imgProfile)
        txtNickname.text = data.nick_name


        when(data.provider) {
            "default" ->{
                txtEmail.text = data.email
                imgSocialLogo.visibility = View.GONE

            }
            "kakao" ->{
                txtEmail.text = "카카오 로그인"
                imgSocialLogo.visibility = View.VISIBLE
                Glide.with(mContext).load(R.drawable.kakao_logo).into(imgSocialLogo)
            }
            "facebook" -> {
                txtEmail.text = "페이스북 로그인"
                imgSocialLogo.visibility = View.VISIBLE
                Glide.with(mContext).load(R.drawable.facebook_logo).into(imgSocialLogo)
            }
            "naver" -> {
                txtEmail.text = "네이버 로그인"
                imgSocialLogo.visibility = View.VISIBLE
                Glide.with(mContext).load(R.drawable.naver_logo).into(imgSocialLogo)
            }
            else ->{

            }
        }


        return row

    }
}