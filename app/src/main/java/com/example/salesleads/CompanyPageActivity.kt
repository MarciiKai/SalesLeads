package com.example.salesleads

import android.os.Bundle
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.salesleads.classes.CompanyData
import com.example.salesleads.databinding.ActivityCompanyPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CompanyPageActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityCompanyPageBinding
    private lateinit var companyNameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var image: ImageView
    private lateinit var auth:FirebaseAuth
    private lateinit var user:String
    private lateinit var databaseReference: DatabaseReference
    private lateinit var uid: String
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCompanyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navView = binding.navView

         auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        databaseReference = FirebaseDatabase.getInstance().getReference("users")
        if (uid.isNotEmpty()){
            getUserData()
        }

        setSupportActionBar(binding.appBarCompanyPage.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
//        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_company_page)


        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_manageuser, R.id.nav_adduser, R.id.nav_salesreport
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun getUserData() {
        databaseReference.child(uid).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue(CompanyData::class.java).toString()
                val navHeaderView = navView.getHeaderView(0)

                companyNameTextView = navHeaderView.findViewById(R.id.profileName_profileFrag)
                emailTextView = navHeaderView.findViewById(R.id.profileEmail_profileFrag)
                image = navHeaderView.findViewById(R.id.profileImage_profileFrag)

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.company_page, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_company_page)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}

