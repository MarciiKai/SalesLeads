package com.example.salesleads.ui.adduser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.example.salesleads.R
import com.example.salesleads.classes.UserData
import com.example.salesleads.databinding.FragmentAdduserBinding
import com.google.firebase.database.FirebaseDatabase

class AddUserFragment : Fragment() {

    private var _binding: FragmentAdduserBinding? = null
    private lateinit var navController: NavController

    private val binding get() = _binding!!
    private val usersRef = FirebaseDatabase.getInstance().getReference("salesperson")

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
        binding.Submitbtn.setOnClickListener {

            val firstname = binding.edtFirstName.text.toString()
            val lastname = binding.edtLastName.text.toString()
//            val userId = binding.edtUserId.text.toString()
            val email = binding.edtEmailAddress.text.toString()

            if (firstname.isNotEmpty() && lastname.isNotEmpty() && email.isNotEmpty()) {
                val userId = usersRef.push().key // Generate a unique ID for the user

                val user = UserData(userId, firstname, lastname, email)

                usersRef.child(userId!!).setValue(user)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // User added successfully
                            binding.edtFirstName.setText("")
                            binding.edtLastName.setText("")
//                            binding.edtUserId.setText("")
                            binding.edtEmailAddress.setText("")
                            navController.navigate(R.id.action_nav_gallery_to_nav_slideshow)
                        } else {
                            // Error adding user
                            // Handle the error accordingly
                        }
                    }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
