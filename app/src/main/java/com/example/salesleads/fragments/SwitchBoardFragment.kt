package com.example.salesleads.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsAnimationController
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.salesleads.R
import com.example.salesleads.databinding.FragmentSwitchBoardBinding
import com.google.firebase.auth.FirebaseAuth

class SwitchBoardFragment : Fragment() {
    private lateinit var navController: NavController
    private lateinit var binding: FragmentSwitchBoardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSwitchBoardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        binding.btnCustomer.setOnClickListener {
            navController.navigate(R.id.action_switchBoardFragment_to_signUpFragment)
        }
        binding.btnCompany.setOnClickListener {
            navController.navigate(R.id.action_switchBoardFragment_to_companyFragment)
        }
    }

    private fun init(view: View) {
        navController = Navigation.findNavController(view)

    }
}