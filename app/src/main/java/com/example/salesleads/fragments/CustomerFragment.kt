package com.example.salesleads.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.salesleads.R
import com.example.salesleads.databinding.FragmentCustomerBinding
import com.example.salesleads.fragments.ToDoDialogFragment.Companion.TAG
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class CustomerFragment : Fragment() {
    private lateinit var navController: NavController
    private lateinit var binding: FragmentCustomerBinding
    private lateinit var radGroup: RadioGroup

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCustomerBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
            binding.btnSubmit.setOnClickListener {
                val db = Firebase.firestore
                val firstName = binding.edtFirstName.text.toString()
                val lastName = binding.edtLastName.text.toString()
                val city = binding.edtCity.text.toString()
                val phone = binding.edtPhone.text.toString()
                val address = binding.edtAddress.text.toString()
                val radioGroup =  when (radGroup.checkedRadioButtonId) {
                    R.id.radMale -> "Male"
                    R.id.radFemale -> "Female"
                    R.id.radNotSay -> "Prefer not to say"
                    else -> ""
                }

                val user = hashMapOf(
                    "firstname" to firstName,
                    "lastname" to lastName,
                    "address" to address,
                    "city" to city,
                    "phone" to phone,
                    "gender" to radioGroup
                )

                db.collection("user")
                    .add(user!!)
                    .addOnSuccessListener {
                        navController.navigate(R.id.action_customerFragment_to_homePageActivity)
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document", e)
                    }
    }
}

    private fun init(view: View) {
        navController = Navigation.findNavController(view)
        radGroup = view.findViewById(R.id.radGroup)


    }
}
