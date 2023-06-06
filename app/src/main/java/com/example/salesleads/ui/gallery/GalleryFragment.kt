package com.example.salesleads.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.salesleads.databinding.FragmentGalleryBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.File

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        binding.Submitbtn.setOnClickListener {
            val db = Firebase.firestore
            val firstname = binding.edtFirstName.text.toString()
            val lastname = binding.edtLastName.text.toString()
            val phone = binding.editPhone.text.toString()
            val email = binding.edtEmailAddress.text.toString()

            val salesperson = hashMapOf(
                "firstname" to firstname,
                "lastname" to lastname,
                "email" to email,
                "phone" to phone
            )
            db.collection("salesperson")
                .add(salesperson)
                .addOnSuccessListener {

                }
        }
        }
    private fun init(){
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}