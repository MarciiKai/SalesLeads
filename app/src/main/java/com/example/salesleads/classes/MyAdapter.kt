package com.example.salesleads.classes

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.salesleads.R
import de.hdodenhof.circleimageview.CircleImageView
import java.util.ArrayList

class MyAdapter(private val userList: ArrayList<UserData> ) : RecyclerView.Adapter<MyAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_item,
        parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
       return userList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = userList[position]

        holder.firstname.text = currentItem.firstname
        holder.lastname.text = currentItem.lastname
        holder.email.text = currentItem.email


    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val firstname : TextView =  itemView.findViewById(R.id.txtfnam)
        val lastname : TextView = itemView.findViewById(R.id.txtnam)
        val email : TextView = itemView.findViewById(R.id.edemail)

    }

}