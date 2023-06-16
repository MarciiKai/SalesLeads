package com.example.salesleads.classes

import android.media.Image
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.salesleads.R
import com.example.salesleads.ui.EditUserFragment
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
        Glide.with(holder.itemView.context)
            .load(currentItem.imageURL)
            .into(holder.profileImage)

        holder.btnEdit.setOnClickListener {
            val clickedItem = userList[position]

            val fetchedData = fetchData(clickedItem)

            val bundle = Bundle()
            bundle.putParcelable("data", fetchedData)

            val editingFragment = EditUserFragment()
            editingFragment.arguments = bundle

            val fragmentManager = (holder.itemView.context as AppCompatActivity).supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.container, editingFragment)
                .commit()

        }


    }

    private fun fetchData(clickedItem: UserData): Parcelable? {
            return null
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val firstname : TextView =  itemView.findViewById(R.id.txtfnam)
        val lastname : TextView = itemView.findViewById(R.id.txtnam)
        val email : TextView = itemView.findViewById(R.id.edemail)
        val profileImage: CircleImageView = itemView.findViewById(R.id.img1)
        val btnEdit : Button = itemView.findViewById(R.id.btnEdit)


    }


}