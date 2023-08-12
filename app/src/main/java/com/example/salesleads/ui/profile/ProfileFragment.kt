package com.example.salesleads.ui.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.example.buynow.data.local.room.Card.CardViewModel
import com.example.salesleads.R
import com.example.salesleads.activities.SettingsActivity
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import de.hdodenhof.circleimageview.CircleImageView
import io.grpc.Context.Storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.UUID

class ProfileFragment : Fragment() {

    lateinit var animationView: LottieAnimationView

    lateinit var profileImage_profileFrag: CircleImageView

    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private val storageReference: StorageReference = FirebaseStorage.getInstance().reference

    lateinit var uploadImage_profileFrag: Button
    lateinit var profileName_profileFrag: TextView
    lateinit var profileEmail_profileFrag: TextView

    private lateinit var cardViewModel: CardViewModel

    private val userReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var cards: Int = 0

    lateinit var linearLayout2: LinearLayout
    lateinit var linearLayout3: LinearLayout
    lateinit var linearLayout4: LinearLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        profileImage_profileFrag = view.findViewById(R.id.profileImage_profileFrag)
        val settingCd_profileFrag = view.findViewById<CardView>(R.id.settingCd_profileFrag)
        uploadImage_profileFrag = view.findViewById(R.id.uploadImage_profileFrag)
        profileName_profileFrag = view.findViewById(R.id.profileName_profileFrag)
        profileEmail_profileFrag = view.findViewById(R.id.profileEmail_profileFrag)
        animationView = view.findViewById(R.id.animationView)
        linearLayout2 = view.findViewById(R.id.linearLayout2)
        linearLayout3 = view.findViewById(R.id.linearLayout3)
        linearLayout4 = view.findViewById(R.id.linearLayout4)
//        val paymentMethod_ProfilePage = view.findViewById<CardView>(R.id.paymentMethod_ProfilePage)
//        val cardsNumber_profileFrag: TextView = view.findViewById(R.id.cardsNumber_profileFrag)

        cardViewModel = ViewModelProvider(this).get(CardViewModel::class.java)

        cardViewModel.allCards.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            cards = it.size
        })

//        if(cards == 0){
//            cardsNumber_profileFrag.text = "You Have no Cards."
//        }
//        else{
//
//            cardsNumber_profileFrag.text = "You Have "+ cards.toString() + " Cards."
//        }

//        shippingAddressCard_ProfilePage.setOnClickListener {
//            startActivity(Intent(context, ShipingAddressActivity::class.java))
//        }
//
//        paymentMethod_ProfilePage.setOnClickListener {
//            startActivity(Intent(context, PaymentMethodActivity::class.java))
//        }

        hideLayout()



        uploadImage_profileFrag.visibility = View.GONE


        getUserData()

        uploadImage_profileFrag.setOnClickListener {
            uploadImage()
        }

        settingCd_profileFrag.setOnClickListener {
            val intent = Intent(context, SettingsActivity::class.java)
            startActivity(intent)
        }



        profileImage_profileFrag.setOnClickListener {

            val popupMenu: PopupMenu = PopupMenu(context,profileImage_profileFrag)

            popupMenu.menuInflater.inflate(R.menu.profile_photo_storage,popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when(item.itemId) {
                    R.id.galleryMenu ->
                        launchGallery()
                    R.id.cameraMenu ->
                        uploadImage()

                }
                true
            })
            popupMenu.show()

        }

        return view
    }

    private fun hideLayout(){
        animationView.playAnimation()
        animationView.loop(true)
        linearLayout2.visibility = View.GONE
        linearLayout3.visibility = View.GONE
        linearLayout4.visibility = View.GONE
        animationView.visibility = View.VISIBLE
    }
    private fun showLayout(){
        animationView.pauseAnimation()
        animationView.visibility = View.GONE
        linearLayout2.visibility = View.VISIBLE
        linearLayout3.visibility = View.VISIBLE
        linearLayout4.visibility = View.VISIBLE
    }

    private fun getUserData() = CoroutineScope(Dispatchers.IO).launch {
        try {
            val userId = firebaseAuth.uid
            val dataSnapshot = userId?.let { userReference.child(it).get().await() }

            val userImage: String? = dataSnapshot?.child("image")?.value?.toString()
            val userName: String? = dataSnapshot?.child("firstname")?.value?.toString()
            val userEmail: String? = dataSnapshot?.child("email")?.value?.toString()

            withContext(Dispatchers.Main) {
                profileName_profileFrag.text = userName
                profileEmail_profileFrag.text = userEmail

                Glide.with(this@ProfileFragment)
                    .load(userImage)
                    .placeholder(R.drawable.ic_profile)
                    .into(profileImage_profileFrag)

                showLayout()
            }
        } catch (e: Exception) {
            // Handle any exceptions that occur
        }
    }


    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    private fun uploadImage(){

        if(filePath != null){
            val ref = storageReference.child("profile_Image/" + UUID.randomUUID().toString())
            val uploadTask = ref?.putFile(filePath!!)

            val urlTask = uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation ref.downloadUrl
            })?.addOnCompleteListener { task: Task<Uri> ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    addUploadRecordToDb(downloadUri.toString())

                    // show save...


                } else {
                    // Handle failures
                }
            }?.addOnFailureListener{

            }
        }else{

            Toast.makeText(context, "Please Upload an Image", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }

            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, filePath)
                profileImage_profileFrag.setImageBitmap(bitmap)
                uploadImage_profileFrag.visibility = View.VISIBLE
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun addUploadRecordToDb(uri: String) = CoroutineScope(Dispatchers.IO).launch {

        try {

            userReference.child(firebaseAuth.uid.toString())
                .child("image")
                .setValue(uri)
                .addOnSuccessListener {
                    // Update successful
                    Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    // Handle any failures that occur during the update process
                    Toast.makeText(context, "Failed to save: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            withContext(Dispatchers.Main){
                Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
            }

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }

    }

}