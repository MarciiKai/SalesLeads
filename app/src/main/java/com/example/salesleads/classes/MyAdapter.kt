package com.example.salesleads.classes

import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.salesleads.R
import com.google.firebase.database.FirebaseDatabase
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.OnItemClickListener
import com.orhanobut.dialogplus.ViewHolder
import de.hdodenhof.circleimageview.CircleImageView
import java.util.ArrayList

@Suppress("NAME_SHADOWING")
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
            val dialog = DialogPlus.newDialog(holder.itemView.context)

                ?.setContentHolder(ViewHolder(R.layout.edit_pop_up)) // Assuming ViewHolder is the appropriate holder class for the content
                ?.setOnItemClickListener(object : OnItemClickListener {
                    override fun onItemClick(
                        dialog: DialogPlus,
                        item: Any,
                        view: View,
                        position: Int
                    ) {
                        // Handle item click event
                    }
                })
                ?.setExpanded(
                    true,
                    1200
                ) // This will enable the expand feature, (similar to android L share dialog)
                ?.create()
//
            val holderView: View? = dialog?.holderView

            val fname: EditText? = holderView?.findViewById(R.id.updateFirstname)
            val lname: EditText? = holderView?.findViewById(R.id.updateLastname)
            val upemail: EditText? = holderView?.findViewById(R.id.updateEmail)
            val image: ImageView? = holderView?.findViewById(R.id.updateImage)
            if (image != null) {
                Glide.with(holder.itemView.context)
                    .load(currentItem.imageURL)
                    .timeout(6000)
                    .into(image)
            }


            val btnUpdate: Button? = holderView?.findViewById(R.id.updateButton)

            fname?.setText(currentItem.firstname)
            lname?.setText(currentItem.lastname)
            upemail?.setText(currentItem.email)

            dialog?.show()

            btnUpdate?.setOnClickListener {
                val new: MutableMap<String, Any> = HashMap()
                val fname: EditText? = holderView.findViewById(R.id.updateFirstname)
                val lname: EditText? = holderView.findViewById(R.id.updateLastname)
                val upemail: EditText? = holderView.findViewById(R.id.updateEmail)

                fname?.let {
                    new["firstname"] = it.text.toString()
                }
                lname?.let {
                    new["lastname"] = it.text.toString()
                }
                upemail?.let {
                    new["email"] = it.text.toString()
                }

                val userId = currentItem.userId

                if (userId != null) {
                    FirebaseDatabase.getInstance().getReference("salesperson")
                        .child(userId)
                        .updateChildren(new)
                        .addOnSuccessListener {
                            // Update success callback
                        }
                        .addOnFailureListener {
                            // Update failure callback
                        }
                }

            }
            holder.btnDelete.setOnClickListener {
                val builder: AlertDialog.Builder = AlertDialog.Builder(holder.firstname.context)
                builder.setTitle("Are you sure?")
                builder.setMessage("Delete data cannot be undone")

                builder.setPositiveButton("Delete") { _: DialogInterface?, _: Int ->
                    // Implement the delete functionality here
                    // You can use the position or currentItem to get the corresponding item in the userList
                    val currentItem: UserData = userList[position]
                    val userId: String? = currentItem.userId // Replace `userId` with the actual identifier property in your `UserData` class

                    // Delete the item from the database using the userId or desired identifier
                    if (userId != null) {
                        FirebaseDatabase.getInstance().getReference("salesperson")
                            .child(userId)
                            .removeValue()
                            .addOnSuccessListener {
                                // Deletion success callback
                            }
                            .addOnFailureListener {
                                // Deletion failure callback
                            }
                    }
                }

                builder.setNegativeButton("Cancel") { _: DialogInterface?, _: Int ->
                    // Handle cancel functionality here
                }

                builder.show()
            }
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val firstname : TextView =  itemView.findViewById(R.id.txtfnam)
        val lastname : TextView = itemView.findViewById(R.id.txtnam)
        val email : TextView = itemView.findViewById(R.id.edemail)
        val profileImage: CircleImageView = itemView.findViewById(R.id.img1)
        val btnEdit : Button = itemView.findViewById(R.id.btnEdit)
        val btnDelete : Button = itemView.findViewById(R.id.btnDelete)


    }


}




