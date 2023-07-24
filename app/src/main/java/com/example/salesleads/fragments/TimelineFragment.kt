package com.example.salesleads.fragments

import com.example.salesleads.classes.TimelineAdapter
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
    private lateinit var timelineRecyclerView: RecyclerView
    private lateinit var timelineAdapter: TimelineAdapter
    private lateinit var timelineList: List<UserData> // Replace this with your actual data list

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_timeline, container, false)

        // Initialize your data list (timelineList) with the necessary data
        timelineList = getTimelineList()

        timelineRecyclerView = view.findViewById(R.id.timelineList)
        timelineRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        timelineRecyclerView.setHasFixedSize(true)

        // Make sure timelineList is initialized before passing it to the adapter
        timelineAdapter = TimelineAdapter(requireContext(), timelineList, null)
        timelineRecyclerView.adapter = timelineAdapter

        return view
    }

    private fun getTimelineList(): List<UserData> {
        val userDataList = mutableListOf<UserData>()

        // Create UserData objects and add them to the list
        val userData1 = UserData(firstname = String())
        userDataList.add(userData1)

        val userData2 = UserData(lastname = String())
        userDataList.add(userData2)

        // Add more UserData objects as needed

        return userDataList
    }

}



