package com.example.salesleads.ui.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.example.salesleads.R

class FavouriteFragment :Fragment() {

    lateinit var animationView: LottieAnimationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_favourite, container, false)


        animationView = view.findViewById(R.id.animationViewFavPage)

        animationView.playAnimation()
        animationView.loop(true)

        return view
    }


}