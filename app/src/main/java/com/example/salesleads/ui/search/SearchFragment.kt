package com.example.salesleads.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.salesleads.R
import com.example.salesleads.databinding.FragmentSearchBinding
import com.google.firebase.auth.FirebaseAuth


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private lateinit var auth: FirebaseAuth
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root
        auth = FirebaseAuth.getInstance()

//        val txtsignOut : TextView = binding.txtsignOut
//        txtsignOut.setOnClickListener { signOut() }
        return root
    }

//    fun signOut(){
//        auth.signOut()
//        val navController = findNavController()
//        navController.navigate(R.id.action_navigation_dashboard_to_signInFragment2)
//    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    }

