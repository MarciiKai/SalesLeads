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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class CompanyFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var mAuth: FirebaseAuth
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
            val companyName = binding.edtCompanyName.text.toString().trim()
            val companyEmail = binding.edtCompanyEmail.text.toString().trim()
            val companyPassword = binding.edtCompanyPassword.text.toString().trim()
            val companyCity = binding.edtCompanyCity.text.toString().trim()
            val companyAddress = binding.edtCompanyAddress.text.toString().trim()

            if (companyName.isNotBlank() && companyEmail.isNotBlank() && companyPassword.isNotBlank() && companyCity.isNotBlank() && companyAddress.isNotBlank()) {
                registerCompany(companyEmail, companyAddress, companyCity, companyName, companyPassword)
            } else {
                binding.edtCompanyName.error = "Fields cannot be empty"
            }
        }
    }

    private fun registerCompany(
        companyEmail: String,
        companyAddress: String,
        companyCity: String,
        companyName: String,
        companyPassword: String
    ) {
        mAuth.createUserWithEmailAndPassword(companyEmail, companyPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentUser = mAuth.currentUser
                    val uid = currentUser?.uid

                    val usersRef = FirebaseDatabase.getInstance().getReference("users")
                    val userData = mapOf(
                        "userType" to "company",
                        "email" to companyEmail,
                        "name" to companyName,
                        "address" to companyAddress,
                        "city" to companyCity
                    )

                    if (uid != null) {
                        usersRef.child(uid).setValue(userData)
                            .addOnSuccessListener {
                                navController.navigate(R.id.action_companyFragment_to_companyPageActivity)
                            }
                            .addOnFailureListener { exception ->
                                Log.e("CompanyRegistration", "Error registering company: $exception")
                            }
                    }
                } else {
                    Log.e("CompanyRegistration", "Error registering company: ${task.exception}")
                }
            }
    }

    private fun init(view: View) {
        navController = Navigation.findNavController(view)
        mAuth = FirebaseAuth.getInstance()
    }
}