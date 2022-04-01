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

class AttendanceSpinnerAdapter(
    val mContext: Context,
    resId: Int,
    val mList: List<UserData>
): ArrayAdapter<UserData>(mContext, resId, mList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var tempRow = convertView
        if (tempRow == null) {
            tempRow =
                LayoutInflater.from(mContext).inflate(R.layout.attendance_spinner_list_item, null)
        }

        val row = tempRow!!

        val data = mList[position]

        val imgProfile = row.findViewById<ImageView>(R.id.imgProfile)
        val txtName = row.findViewById<TextView>(R.id.txtName)

        txtName.text = data.nick_name
        Glide.with(mContext).load(data.profile_img).into(imgProfile)


        return row

    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {

        var tempRow = convertView
        if (tempRow == null) {
            tempRow =
                LayoutInflater.from(mContext).inflate(R.layout.attendance_spinner_list_item, null)
        }

        val row = tempRow!!

        val data = mList[position]

        val imgProfile = row.findViewById<ImageView>(R.id.imgProfile)
        val txtName = row.findViewById<TextView>(R.id.txtName)

        txtName.text = data.nick_name
        Glide.with(mContext).load(data.profile_img).into(imgProfile)


        return row

    }
}

