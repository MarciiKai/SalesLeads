package com.example.salesleads.classes

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.salesleads.R
import de.hdodenhof.circleimageview.CircleImageView
import java.util.ArrayList

class UserAdapter(private val userList: ArrayList<UserData>,
                  private  val navController: NavController) : RecyclerView.Adapter<UserAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.salesperson_list,
            parent, false
        )
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = userList[position]

        holder.firstname.text = currentItem.firstname
        holder.lastname.text = currentItem.lastname
        holder.compName.text = currentItem.compName


        Glide.with(holder.itemView.context)
            .load(currentItem.imageURL)
            .into(holder.profileImage)

        holder.btnRate.setOnClickListener {
            navController.navigate(R.id.action_navigation_search_to_timelineFragment)

        }
    }

        class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val firstname: TextView = itemView.findViewById(R.id.txtfnam)
            val lastname: TextView = itemView.findViewById(R.id.txtnam)
            val compName: TextView = itemView.findViewById(R.id.edtUserComp)
            val profileImage: CircleImageView = itemView.findViewById(R.id.img1)
            val btnRate: Button = itemView.findViewById(R.id.btnRate)


        }

}

