package com.example.salesleads.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import com.example.salesleads.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import android.widget.Toast


class SettingsActivity : AppCompatActivity() {

    lateinit var nameEt_SettingsPage:EditText
    lateinit var EmailEt_SettingsPage:EditText
    lateinit var saveSetting_SettingsBtn:Button

    private val userReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        nameEt_SettingsPage = findViewById(R.id.nameEt_SettingsPage)
        EmailEt_SettingsPage = findViewById(R.id.EmailEt_SettingsPage)
        saveSetting_SettingsBtn = findViewById(R.id.saveSetting_SettingsBtn)
        val backIv_ProfileFrag:ImageView = findViewById(R.id.backIv_ProfileFrag)


        backIv_ProfileFrag.setOnClickListener {
            onBackPressed()
        }

        getUserData()

        saveSetting_SettingsBtn.setOnClickListener {
            textCheck()
        }

        textAutoCheck()
    }


    private fun getUserData() = CoroutineScope(Dispatchers.IO).launch {
        try {

            val userId = firebaseAuth.uid
            val dataSnapshot = userId?.let { userReference.child(it).get().await() }

            val userName:String = dataSnapshot?.child("firstname").toString()
            val userEmail:String = dataSnapshot?.child("email").toString()


            withContext(Dispatchers.Main){


            }


        }catch (e:Exception){

        }
    }

    private fun textCheck() {
        if (nameEt_SettingsPage.text.isEmpty()) {
            Toast.makeText(this, "Name Can't be Empty", Toast.LENGTH_SHORT).show()
            return
        }
        if (EmailEt_SettingsPage.text.isEmpty()) {
            Toast.makeText(this, "Email Can't be Empty", Toast.LENGTH_SHORT).show()
            return
        }

        saveNameAndEmailToFirebase()
    }

    private fun saveNameAndEmailToFirebase() = CoroutineScope(Dispatchers.IO).launch {
        try {
            val userId = firebaseAuth.uid
            val userRef = userId?.let { userReference.child(it) }

            userRef?.child("firstname")?.setValue(nameEt_SettingsPage.text.toString())?.await()
            userRef?.child("email")?.setValue(EmailEt_SettingsPage.text.toString())?.await()

            withContext(Dispatchers.Main) {
                Toast.makeText(this@SettingsActivity, "Saved", Toast.LENGTH_SHORT).show()
                saveSetting_SettingsBtn.visibility = View.GONE
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@SettingsActivity, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun textAutoCheck() {

        nameEt_SettingsPage.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                saveSetting_SettingsBtn.visibility = View.VISIBLE
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if(count > 1){
                    saveSetting_SettingsBtn.visibility = View.VISIBLE
                }
            }
        })

        EmailEt_SettingsPage.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                saveSetting_SettingsBtn.visibility = View.VISIBLE
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if(count > 1){
                    saveSetting_SettingsBtn.visibility = View.VISIBLE
                }
            }
        })
    }
}