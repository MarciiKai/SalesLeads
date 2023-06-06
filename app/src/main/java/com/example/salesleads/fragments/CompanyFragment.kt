package com.example.salesleads.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.salesleads.R
import com.example.salesleads.databinding.FragmentCompanyBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class CompanyFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var binding: FragmentCompanyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCompanyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        binding.btnSubmit.setOnClickListener {
            val db = Firebase.firestore
            val companyName = binding.edtCompanyName.text.toString()
            val city = binding.edtCity.text.toString()
            val phone = binding.edtPhone.text.toString()
            val address = binding.edtAddress.text.toString()

            val company = hashMapOf(
                "companyName" to companyName,
                "city" to city,
                "phone" to phone,
                "address" to address
            )
            db.collection("company")
                .add(company)
                .addOnSuccessListener {
                    navController.navigate(R.id.action_companyFragment_to_companyPageActivity)
                }
                .addOnFailureListener{e ->
                    Log.w(ToDoDialogFragment.TAG, "Error adding document", e)
                }

        }

    }
    private fun init(view:View){
        navController = Navigation.findNavController(view)
    }
}

