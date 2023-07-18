package com.example.salesleads.ui.leads

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.salesleads.R
import com.example.salesleads.classes.UserAdapter
import com.example.salesleads.classes.UserData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class LeadsFragment: Fragment() {

    private lateinit var dbRef: DatabaseReference
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userArrayList: ArrayList<UserData>
    private lateinit var userAdapter: UserAdapter
    private lateinit var valueEventListener: ValueEventListener
    private lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_leads, container, false)
        userRecyclerView = view.findViewById(R.id.salesperson_list)
        userRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        userRecyclerView.setHasFixedSize(true)

        navController = findNavController()


        userArrayList = arrayListOf()
        userAdapter = UserAdapter(userArrayList, navController)


        userRecyclerView.adapter = userAdapter

        dbRef = FirebaseDatabase.getInstance().getReference("salesperson")
        valueEventListener = object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                userArrayList.clear()
                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(UserData::class.java)
                    user?.let {
                        userArrayList.add(it)
                    }
                }
                userAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle the error here
            }
        }

        return view

    }

    override fun onStart() {
        super.onStart()
        dbRef.addValueEventListener(valueEventListener)
    }

    override fun onStop() {
        super.onStop()
        dbRef.removeEventListener(valueEventListener)
    }
}

