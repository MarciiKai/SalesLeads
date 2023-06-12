package com.example.salesleads.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.salesleads.R
import com.example.salesleads.databinding.FragmentSignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class SignInFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: FragmentSignInBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)

        val currentUser: FirebaseUser? = mAuth.currentUser
        if (currentUser != null) {
            // User is already authenticated, navigate to the main screen
            navigateToMainScreen(currentUser)
            return
        }

        binding.textViewSignUp.setOnClickListener {
            navController.navigate(R.id.action_signInFragment_to_switchBoardFragment2)
        }

        binding.nextBtn.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passEt.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty())
                loginUser(email, pass)
            else
                Toast.makeText(context, "Empty fields are not allowed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loginUser(email: String, pass: String) {
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val currentUser = mAuth.currentUser
                if (currentUser != null) {
                    navigateToMainScreen(currentUser)
                } else {
                    Toast.makeText(context, "Failed to retrieve user information", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, task.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToMainScreen(currentUser: FirebaseUser) {
        val uid = currentUser.uid
        val usersRef = FirebaseDatabase.getInstance().getReference("users")
        usersRef.child(uid).get().addOnSuccessListener { snapshot ->

            when (snapshot.child("userType").value as? String) {
                "customer" -> {
                    navController.navigate(R.id.action_signInFragment_to_homePageActivity)
                }
                "company" -> {
                    navController.navigate(R.id.action_signInFragment_to_companyPageActivity)
                }
                else -> {
                    Toast.makeText(context, "Invalid user type", Toast.LENGTH_SHORT).show()
                }
            }
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to retrieve user information", Toast.LENGTH_SHORT).show()
        }
    }

    private fun init(view: View) {
        navController = Navigation.findNavController(view)
        mAuth = FirebaseAuth.getInstance()
    }
}