package com.gaurav.serviceproviderapp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gaurav.serviceproviderapp.databinding.CategoryRevnameBinding


class UserAdapter(var userList: ArrayList<UserModel>, val list: UserInterface):RecyclerView.Adapter<UserAdapter.viewHolder>(){
    class viewHolder (val binding: CategoryRevnameBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
val binding=CategoryRevnameBinding.inflate(LayoutInflater.from(parent.context),parent,false)
    return viewHolder(binding)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.binding.tvName.text=userList[position].name
holder.itemView.setOnClickListener{
    list.ListClicked(position)
}
    }

    override fun getItemCount(): Int {
     return userList.size
    }

}
