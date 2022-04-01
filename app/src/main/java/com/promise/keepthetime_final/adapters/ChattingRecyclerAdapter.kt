package com.promise.keepthetime_final.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.promise.keepthetime_final.R
import com.promise.keepthetime_final.datas.ChattingData

class ChattingRecyclerAdapter(
    val mContext: Context,
    val mList: List<ChattingData>
): RecyclerView.Adapter<ChattingRecyclerAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val txtContent = view.findViewById<TextView>(R.id.txtContent)
        val txtCreateAt = view.findViewById<TextView>(R.id.txtCreateAt)

        fun bind(data: ChattingData) {

            txtContent.text = data.content
            txtCreateAt.text = data.createAt
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val row = LayoutInflater.from(mContext).inflate(R.layout.chatting_list_item, parent, false)
        return MyViewHolder(row)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = mList[position]

        holder.bind(data)
    }

    override fun getItemCount()= mList.size
}