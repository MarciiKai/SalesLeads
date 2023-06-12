package com.example.salesleads.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.salesleads.R
import com.example.salesleads.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpFragment : Fragment() {
    private lateinit var navController: NavController
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)

        binding.textViewSignIn.setOnClickListener {
            navController.navigate(R.id.action_signUpFragment_to_signInFragment)
        }

        binding.nextBtn.setOnClickListener {
            val firstname = binding.edtFirstName.text.toString().trim()
            val lastname = binding.edtLastName.text.toString().trim()
            val email = binding.emailEt.text.toString().trim()
            val password = binding.passEt.text.toString().trim()
            val verifyPassword = binding.verifyPassEt.text.toString().trim()
            val phone = binding.edtPhone.text.toString().trim()
            val city = binding.edtCity.text.toString().trim()
            val address = binding.edtAddress.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty() && verifyPassword.isNotEmpty() &&
                firstname.isNotEmpty() && lastname.isNotEmpty() && phone.isNotEmpty() &&
                city.isNotEmpty() && address.isNotEmpty()
            ) {
                if (password == verifyPassword) {
                    registerUser(email, password, firstname, lastname, phone, city, address)
                } else {
                    Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerUser(
        email: String,
        password: String,
        firstname: String,
        lastname: String,
        phone: String,
        city: String,
        address: String
    ) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    val userId = user?.uid

                    val userData = hashMapOf(
                        "userType" to "customer",
                        "email" to email,
                        "password" to password,
                        "firstname" to firstname,
                        "lastname" to lastname,
                        "phone" to phone,
                        "city" to city,
                        "address" to address
                    )

                    if (userId != null) {
                        database.child("users").child(userId).setValue(userData)
                            .addOnSuccessListener {
                                navController.navigate(R.id.action_signUpFragment_to_signInFragment)
                            }
                            .addOnFailureListener { exception ->
                                Toast.makeText(
                                    context,
                                    "Error registering user: ${exception.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                } else {
                    Toast.makeText(context, "Error registering user: ${task.exception}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun init(view: View) {
        navController = Navigation.findNavController(view)
        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
    }
}
