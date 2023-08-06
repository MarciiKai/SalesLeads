package com.example.salesleads.ui.adduser

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.provider.MediaStore
import com.example.salesleads.classes.UserData
import com.example.salesleads.databinding.FragmentAdduserBinding
import com.google.firebase.database.FirebaseDatabase

class AddUserFragment : Fragment() {

    private var _binding: FragmentAdduserBinding? = null
    private val binding get() = _binding!!
    private var selectedImageUri: Uri? = null

    private val PERMISSION_REQUEST_CODE = 1001



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

        val pickImageLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult? ->
                if (result?.resultCode == Activity.RESULT_OK) {
                    val data = result.data
                    selectedImageUri = data?.data
                    binding.uploadImage.setImageURI(selectedImageUri)
                }
            }

        binding.uploadImage.setOnClickListener {
            val photoPicker = Intent(Intent.ACTION_OPEN_DOCUMENT)
            photoPicker.addCategory(Intent.CATEGORY_OPENABLE)
            photoPicker.type = "image/*"
            pickImageLauncher.launch(photoPicker)
        }

        binding.Submitbtn.setOnClickListener {
            val firstname = binding.edtFirstName.text.toString()
            val lastname = binding.edtLastName.text.toString()
            val email = binding.edtEmailAddress.text.toString()
            val compName = binding.edtUserComp.text.toString()

            if (firstname.isNotEmpty() && lastname.isNotEmpty() && email.isNotEmpty() && selectedImageUri != null) {
                uploadDataWithImage(firstname, lastname, email, compName)
            }
        }
    }

    private fun uploadDataWithImage(firstname: String, lastname: String, email: String, compName: String) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("salesperson")
        val userKey = databaseReference.push().key

        if (userKey != null) {
            val dataClass = UserData(
                userId = userKey,
                firstname = firstname,
                lastname = lastname,
                email = email,
                compName = compName,
                imageURL = selectedImageUri.toString()
            )

            databaseReference.child(userKey).setValue(dataClass)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Data uploaded successfully
                        binding.uploadImage.setImageDrawable(null)
                        binding.edtFirstName.setText("")
                        binding.edtLastName.setText("")
                        binding.edtEmailAddress.setText("")
                        binding.edtUserComp.setText("")
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
