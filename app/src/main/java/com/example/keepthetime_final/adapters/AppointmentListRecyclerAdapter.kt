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
import com.example.keepthetime_final.AppointmentDetailActivity
import com.example.keepthetime_final.R
import com.example.keepthetime_final.ViewMapActivity
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
        val btnMoveDetailAppointment = view.findViewById<LinearLayout>(R.id.btnMoveDetailAppointment)

        fun bind(data: AppointmentData){

            btnMoveDetailAppointment.setOnClickListener {
                val myIntent = Intent(mContext, AppointmentDetailActivity::class.java)
                myIntent.putExtra("appointmentData",data)

                mContext.startActivity(myIntent)
            }
            txtAppointmentTitle.text = data.title
            txtPlaceName.text = data.place

//            서버가 주는 datetime(Date형태로 내려옴)

//            출력하고 싶은 datetime(String - 22년 3월 5일 오후 2시 30분 양식) - format활용
            val sdf = SimpleDateFormat("yy년 M월 d일 a h시 m분")

            txtDateTime.text = sdf.format(data.datetime)

            imgMap.setOnClickListener {
                val myIntent = Intent(mContext, ViewMapActivity::class.java)
                myIntent.putExtra("appointment", data)
                mContext.startActivity(myIntent) //어댑터에서는 mContext로
            }
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