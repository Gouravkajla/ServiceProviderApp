package com.gaurav.serviceproviderapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gaurav.serviceproviderapp.databinding.FragmentCustomerLayoutBinding


class CustomerAdapter(
    var userList: ArrayList<CustomerModel>,
    var mainActivity: MainActivity,
    fragmentCustomerList: FragmentCustomerList,

    ) : RecyclerView.Adapter<CustomerAdapter.viewHolder>() {
    class viewHolder(val binding: FragmentCustomerLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val binding=FragmentCustomerLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewHolder(binding)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {

       holder.binding.tcCustomer.text=userList[position].name
    /*    holder.binding.btnAcceptReject.setOnClickListener {
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
        }*/
    }

    override fun getItemCount(): Int {
        return userList.size
    }

}
