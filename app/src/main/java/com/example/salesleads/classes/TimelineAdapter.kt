package com.example.salesleads.classes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.salesleads.R

class TimelineAdapter(private val animationResIds: List<Int>) :
    RecyclerView.Adapter<TimelineAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val animationView: LottieAnimationView = itemView.findViewById(R.id.animationViewPage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.timeline_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val animationResId = animationResIds[position]
        holder.animationView.setAnimation(animationResId)
        holder.animationView.playAnimation()
    }

    override fun getItemCount(): Int {
        return animationResIds.size
    }
}
