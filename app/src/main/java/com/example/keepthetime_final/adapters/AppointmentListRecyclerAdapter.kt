package com.example.keepthetime_final.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.keepthetime_final.R
import com.example.keepthetime_final.datas.AppointmentData
import com.example.keepthetime_final.datas.UserData
import java.text.SimpleDateFormat

class AppointmentListRecyclerAdapter(
    val mContext: Context,
    val mList: List<AppointmentData>
): RecyclerView.Adapter<AppointmentListRecyclerAdapter.MyViewHolder>() {

    inner class MyViewHolder(view:View) : RecyclerView.ViewHolder(view) {

        val txtAppointmentTitle = view.findViewById<TextView>(R.id.txtAppointmentTitle)
        val txtDateTime = view.findViewById<TextView>(R.id.txtDateTime)
        val txtPlaceName = view.findViewById<TextView>(R.id.txtPlaceName)
        val imgMap = view.findViewById<ImageView>(R.id.imgMap)

        fun bind(data: AppointmentData){
            txtAppointmentTitle.text = data.title
            txtPlaceName.text = data.place

//            서버가 주는 datetime(String - 2022-03-15 10:57:23 양식)
//            중간에 Calendar / Date형태로 변환 =>
//            출력하고 싶은 datetime(String - 22년 3월 5일 오후 2시 30분 양식)
            val sdf = SimpleDateFormat("yy년 M월 d일 a h시 m분")
            sdf.format(data.datetime)
            txtDateTime.text = data.created_at
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(mContext).inflate(R.layout.appointment_list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val data = mList[position]
        holder.bind(data)

    }

    override fun getItemCount() = mList.size
}