package com.gaurav.serviceproviderapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gaurav.serviceproviderapp.databinding.CategoryRevnameBinding


class CategoryAdapter(var userList: ArrayList<CategoryModel>,var mainActivity: MainActivity, val list: CategoryInterface):RecyclerView.Adapter<CategoryAdapter.viewHolder>(){
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
        Glide.with(mainActivity)
            .load(userList[position].image)
            .circleCrop()
            .into(holder.binding.ivImage)
    }

    override fun getItemCount(): Int {
     return userList.size
    }

}
