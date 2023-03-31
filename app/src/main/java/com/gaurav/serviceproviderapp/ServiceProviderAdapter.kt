package com.gaurav.serviceproviderapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gaurav.serviceproviderapp.databinding.CategoryRevnameBinding
import com.gaurav.serviceproviderapp.databinding.LayoutServiceProviderBinding


class ServiceProviderAdapter(
    var userList: ArrayList<ServiceProviderModel>,
    var mainActivity: MainActivity,
    val list: ServiceProviderInterface
) : RecyclerView.Adapter<ServiceProviderAdapter.viewHolder>() {
    class viewHolder(val binding: LayoutServiceProviderBinding) : RecyclerView.ViewHolder(binding.root) {


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val binding =
            LayoutServiceProviderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewHolder(binding)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {

        holder.binding.tvName.text = userList[position].name
        holder.binding.btnAcceptReject.setOnClickListener {
            list.ServiceProviderClicked(position)
        }
        Glide.with(mainActivity)
            .load(userList[position].image)
            .circleCrop()
            .into(holder.binding.ivImage)
        if(userList[position].currentStatus == 0){
            holder.binding.btnAcceptReject.visibility = View.VISIBLE
        }else{
            holder.binding.btnAcceptReject.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

}
