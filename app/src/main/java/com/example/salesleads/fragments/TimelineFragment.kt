package com.example.salesleads.fragments

import TimelineAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.salesleads.R
import com.example.salesleads.classes.UserData

class TimelineFragment : Fragment() {

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userArrayList: ArrayList<UserData>
    private lateinit var myAdapter: TimelineAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_timeline, container, false)

        val userRecyclerView: RecyclerView = view.findViewById(R.id.salesperson_list)
        userRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val userArrayList = ArrayList<UserData>()
        userArrayList.add(UserData("Talia", "Sasha", 4.5f, "Sample description"))
        userArrayList.add(UserData("John", "Doe", 3.8f, "Another description"))

        val myAdapter = TimelineAdapter(requireContext(), userArrayList) { selectedUser ->
            // Handle the click event here
            val fragmentManager = parentFragmentManager
            val timelineFragment = TimelineFragment.newInstance(selectedUser.firstname, selectedUser.rating)
            fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, timelineFragment)
                .addToBackStack(null)
                .commit()
        }

        userRecyclerView.adapter = myAdapter

        return view
    }


}
