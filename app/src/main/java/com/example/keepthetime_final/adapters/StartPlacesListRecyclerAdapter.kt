package com.example.keepthetime_final.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.keepthetime_final.R
import com.example.keepthetime_final.datas.PlacesData

class StartPlacesListRecyclerAdapter(
    val mContext: Context,
    val mList: List<PlacesData>
): RecyclerView.Adapter<StartPlacesListRecyclerAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val txtStartPlaceName = view.findViewById<TextView>(R.id.txtStartPlaceName)

        fun bind(data : PlacesData){

            txtStartPlaceName.text = data.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(mContext).inflate(R.layout.my_place_list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val data = mList[position]
        holder.bind(data)
    }

    override fun getItemCount() = mList.size
}