package com.example.salesleads.ui.manageuser


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.salesleads.R
import com.example.salesleads.classes.UserData
import com.example.salesleads.classes.MyAdapter
import com.google.firebase.database.*

class ManageUserFragment : Fragment() {
    private lateinit var dbRef: DatabaseReference
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userArrayList: ArrayList<UserData>
    private lateinit var myAdapter: MyAdapter
    private lateinit var valueEventListener: ValueEventListener


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_manageuser, container, false)
        userRecyclerView = view.findViewById(R.id.userList)
        userRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        userRecyclerView.setHasFixedSize(true)

        userArrayList = arrayListOf()
        myAdapter = MyAdapter(userArrayList)
        userRecyclerView.adapter = myAdapter

        dbRef = FirebaseDatabase.getInstance().getReference("salesperson")
        valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userArrayList.clear()
                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(UserData::class.java)
                    user?.let {
                        userArrayList.add(it)
                    }
                }
                myAdapter.notifyDataSetChanged()
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



