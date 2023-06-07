package com.example.salesleads.ui.adduser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.salesleads.databinding.FragmentAdduserBinding
import com.google.firebase.firestore.FirebaseFirestore

class AddUserFragment : Fragment(){

    private var _binding: FragmentAdduserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdduserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.Submitbtn.setOnClickListener {
//            val db = FirebaseFirestore.getInstance()
//            val firstname = binding.edtFirstName.text.toString()
//            val lastname = binding.edtLastName.text.toString()
//            val phone = binding.editPhone.text.toString()
//            val email = binding.edtEmailAddress.text.toString()
//
//            val salesperson = hashMapOf(
//                "firstname" to firstname,
//                "lastname" to lastname,
//                "email" to email,
//                "phone" to phone
//            )
//            db.collection("salesperson")
//                .add(salesperson!!)
//                .addOnSuccessListener {
//                    // Data uploaded successfully
//                }
//                .addOnFailureListener {
//                    // Error occurred while uploading data
//                }
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

