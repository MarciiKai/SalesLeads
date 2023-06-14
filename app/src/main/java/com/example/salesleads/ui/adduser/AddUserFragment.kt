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
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.example.salesleads.R
import com.example.salesleads.classes.UserData
import com.example.salesleads.databinding.FragmentAdduserBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AddUserFragment : Fragment() {

    private var _binding: FragmentAdduserBinding? = null
    private lateinit var navController: NavController
    private var imageURL: String? = null
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
        // Inside the onViewCreated() method

        binding.uploadImage.setOnClickListener {
            val photoPicker = Intent(Intent.ACTION_PICK)
            photoPicker.type = "image/*"
            activityResultLauncher.launch(photoPicker)
        }

        binding.Submitbtn.setOnClickListener {
            val firstname = binding.edtFirstName.text.toString()
            val lastname = binding.edtLastName.text.toString()
            val email = binding.edtEmailAddress.text.toString()

            if (firstname.isNotEmpty() && lastname.isNotEmpty() && email.isNotEmpty()) {
                if (uri != null) {
                    val storageReference = FirebaseStorage.getInstance().reference.child("salesperson")
                        .child(uri!!.lastPathSegment!!)
                    storageReference.putFile(uri!!).addOnSuccessListener { taskSnapshot ->
                        val uriTask = taskSnapshot.storage.downloadUrl
                        while (!uriTask.isComplete);
                        val urlImage = uriTask.result
                        imageURL = urlImage.toString()

                        // Load image with Glide if URI is not null
                        if (uri != null) {
                            Glide.with(requireContext())
                                .load(uri)
                                .into(binding.uploadImage)
                        }

                        uploadData(firstname, lastname, email)
                    }.addOnFailureListener {
                        // Handle the failure
                    }
                } else {
                    uploadData(firstname, lastname, email)
                }
            }
        }


    }

    private fun uploadData(firstname: String, lastname: String, email: String) {
        val dataClass = UserData(
            firstname,
            lastname,
            email,
            imageURL
        )

        FirebaseDatabase.getInstance().getReference("salesperson")
            .push() // Create a new child node with a unique key
            .setValue(dataClass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Data uploaded successfully with imageURL
                    binding.uploadImage.setImageDrawable(null)
                    binding.edtFirstName.setText("")
                    binding.edtLastName.setText("")
                    binding.edtEmailAddress.setText("")
                    navController.navigate(R.id.action_nav_gallery_to_nav_slideshow)
                } else {
                    // Error uploading data
                    // Handle the error accordingly
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
