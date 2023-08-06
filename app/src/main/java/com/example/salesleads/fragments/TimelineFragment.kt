package com.example.salesleads.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.salesleads.R
import com.example.salesleads.classes.TimelineAdapter

class TimelineFragment : Fragment() {
    private lateinit var timelineRecyclerView: RecyclerView
    private val animationResIds = listOf(R.raw.empty)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_timeline, container, false)

        timelineRecyclerView = view.findViewById(R.id.timelineList)
        timelineRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        timelineRecyclerView.setHasFixedSize(true)

        val timelineAdapter = TimelineAdapter(animationResIds)
        timelineRecyclerView.adapter = timelineAdapter

        return view
    }
}
