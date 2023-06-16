package com.example.salesleads.ui.adduser

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.salesleads.R
import com.example.salesleads.classes.UserData
import com.example.salesleads.databinding.FragmentAdduserBinding
import com.google.firebase.database.FirebaseDatabase

class AddUserFragment : Fragment() {

    private var _binding: FragmentAdduserBinding? = null
    private var uri: Uri? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdduserBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult? ->
            if (result?.resultCode == RESULT_OK) {
                val data = result.data
                uri = data?.data
                binding.uploadImage.setImageURI(uri)
            }
        }

        binding.uploadImage.setOnClickListener {
            val photoPicker = Intent(Intent.ACTION_PICK)
            photoPicker.type = "image/*"
            activityResultLauncher.launch(photoPicker)
        }

        binding.Submitbtn.setOnClickListener {
            val firstname = binding.edtFirstName.text.toString()
            val lastname = binding.edtLastName.text.toString()
            val email = binding.edtEmailAddress.text.toString()
            val imageURL = uri?.toString()

            if (firstname.isNotEmpty() && lastname.isNotEmpty() && email.isNotEmpty() && imageURL != null) {
                uploadDataWithImage(firstname, lastname, email, imageURL)
            }
        }
    }

    private fun uploadDataWithImage(firstname: String, lastname: String, email: String, imageURL: String) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("salesperson")
        val userKey = databaseReference.push().key

        if (userKey != null) {
            val dataClass = UserData(
                userId = userKey,
                firstname = firstname,
                lastname = lastname,
                email = email,
                imageURL = imageURL
            )

            databaseReference.child(userKey).setValue(dataClass)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Data uploaded successfully
                        binding.uploadImage.setImageDrawable(null)
                        binding.edtFirstName.setText("")
                        binding.edtLastName.setText("")
                        binding.edtEmailAddress.setText("")
                    } else {
                        // Error uploading data
                        // Handle the error accordingly
                    }
                }
        } else {
            // Error generating user key
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
