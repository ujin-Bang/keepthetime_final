package com.example.keepthetime_final.adapters

import android.content.Context
import android.content.Intent
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.keepthetime_final.AppointmentDetailActivity
import com.example.keepthetime_final.R
import com.example.keepthetime_final.ViewMapActivity
import com.example.keepthetime_final.datas.AppointmentData
import com.example.keepthetime_final.datas.InvitedFriendData
import com.example.keepthetime_final.datas.UserData
import org.w3c.dom.Text
import java.text.SimpleDateFormat

class InvitedListRecyclerAdapter(
    val mContext: Context,
    val mList: List<InvitedFriendData>
): RecyclerView.Adapter<InvitedListRecyclerAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val imgProfile = view.findViewById<ImageView>(R.id.imgProfile)
        val txtNickname = view.findViewById<TextView>(R.id.txtNickname)
        val txtEmail = view.findViewById<TextView>(R.id.txtEmail)

        fun bind(data: InvitedFriendData) {

            Glide.with(mContext).load(data.profile_img).into(imgProfile)
            txtNickname.text = data.nick_name
            txtEmail.text = data.email
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(mContext).inflate(R.layout.invited_list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = mList[position]
        holder.bind(data)
    }

    override fun getItemCount() = mList.size
    }
