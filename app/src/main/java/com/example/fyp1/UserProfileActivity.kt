package com.example.fyp1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.text.SimpleDateFormat
import java.util.*

class UserProfileActivity : AppCompatActivity() {

    var nMenu: BottomNavigationView? = null
    var iProfile: ImageView? = null
    var tUserID: TextView? = null
    var tFullName: TextView? = null
    var tName: TextInputEditText? = null
    var tEmail: TextInputEditText? = null
    var tPhoneNum: TextInputEditText? = null
    var tDOB: TextInputEditText? = null
    var tAddress: TextInputEditText? = null
    var bUpdate: Button? = null
    lateinit var patientDetails: Patients
    var databaseReference: DatabaseReference? =
        FirebaseDatabase.getInstance().getReference("patient")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        iProfile = findViewById<ImageView?>(R.id.profile_image)
        tUserID = findViewById<TextView?>(R.id.userID)
        tFullName = findViewById<TextView?>(R.id.full_name)
        tName = findViewById<TextInputEditText?>(R.id.textInputEditTextName)
        tEmail = findViewById<TextInputEditText?>(R.id.textInputEditTextEmail)
        tPhoneNum = findViewById<TextInputEditText?>(R.id.textInputEditTextPhoneNumber)
        tDOB = findViewById<TextInputEditText?>(R.id.textInputEditTextBirthday)
        tAddress = findViewById<TextInputEditText?>(R.id.textInputEditTextAddress)
        bUpdate = findViewById<Button?>(R.id.button_update_profile)

        //get data from intent
        val intent = intent
        val userID = intent.getStringExtra("UserID")
        val name = intent.getStringExtra("Name")

        getPatientDetails(userID)

        bUpdate!!.setOnClickListener {
            patientUpdateDialog(patientDetails)
        }
        nMenu = findViewById<View>(R.id.bottomNavigationView) as BottomNavigationView?

        nMenu!!.selectedItemId = R.id.profile

        nMenu!!.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    val intphto = Intent(this, DashboardActivity::class.java)
                    intphto.putExtra("UserID", userID)
                    intphto.putExtra("Name", name)
                    startActivity(intphto)
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.appointment -> {
                    true
                }
                R.id.profile -> {
                    true
                }
            }
            false
        }
    }

    private fun getPatientDetails(id: String) {
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val formatter = SimpleDateFormat("dd-MMMM-yyyy")
        databaseReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.child(id).exists()) {
                    if (!id.isEmpty()) {
                        val patient = dataSnapshot.child(id).getValue(Patients::class.java)!!
                        if (patient.gender.toString() == "M") {
                            profile_image.setImageResource(R.drawable.ic_avat_men)
                        } else {
                            profile_image.setImageResource(R.drawable.ic_avat_women)
                        }
                        val date = dateFormat.parse("${patient.dateOfBirth}")
                        val dateOfBirth = formatter.format(date)

                        tUserID!!.text = patient.userID.toString()
                        tFullName!!.text = patient.name.toString()
                        tName!!.setText(patient.name.toString())
                        tEmail!!.setText(patient.email.toString())
                        tPhoneNum!!.setText(patient.phoneNo.toString())
                        tDOB!!.setText(dateOfBirth)
                        tAddress!!.setText(patient.address.toString())
                        patientDetails = patient

                        Log.i("myInfoTag", "Profile complete")
                    } else {
                        Toast.makeText(this@UserProfileActivity, "Error", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this@UserProfileActivity, "Error", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun patientUpdateDialog(patient: Patients) {


        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.alert_dialog_update_profile, null)
        val tEmail = dialogLayout.findViewById<TextInputEditText>(R.id.email)
        val tAddress = dialogLayout.findViewById<TextInputEditText>(R.id.address)
        val tPhoneNo = dialogLayout.findViewById<TextInputEditText>(R.id.phone_no)

        tEmail.setText("${patient.email}")
        tPhoneNo.setText("${patient.phoneNo}")
        tAddress.setText("${patient.address}")

        builder.setView(dialogLayout)
        builder.setPositiveButton("Update") { _, _ ->
            val ID = patient.userID.toString()
            val email = tEmail.text.toString().trim { it <= ' ' }
            val address = tAddress.text.toString().trim { it <= ' ' }
            val phoneNo = tPhoneNo.text.toString().trim { it <= ' ' }

            if (email == patient.email.toString() && phoneNo == patient.phoneNo.toString() && address == patient.address.toString()) {
                Toast.makeText(this, "Nothing update.", Toast.LENGTH_SHORT).show()
            } else {
                val alertDialog = AlertDialog.Builder(this).create()
                alertDialog.setTitle("Confirmation")
                alertDialog.setMessage("Do you want to update your details?")

                alertDialog.setButton(
                    AlertDialog.BUTTON_POSITIVE, "Yes"
                ) { _, _ ->

                    if (TextUtils.isEmpty(email)) {
                        tEmail.error = "Please enter the email!"
                    } else if (TextUtils.isEmpty(phoneNo)) {
                        tPhoneNo.error = "Please enter the phone number!"
                    } else if (TextUtils.isEmpty(address)) {
                        tPhoneNo.error = "Please enter the address!"
                    } else {
                        databaseReference!!.child(ID).child("phoneNo").setValue(phoneNo)
                        databaseReference!!.child(ID).child("email").setValue(email)
                        databaseReference!!.child(ID).child("address").setValue(address)
                        Log.i("myInfoTag", "completed")
                        //Toast.makeText(this,"${patient.userID} $phoneNo $email",Toast.LENGTH_SHORT).show()
                    }
                }

                alertDialog.setButton(
                    AlertDialog.BUTTON_NEGATIVE, "No"
                ) { _, _ ->
                    Toast.makeText(this, "Nothing update.", Toast.LENGTH_SHORT).show()
                }

                alertDialog.show()

                val btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                val btnNegative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)

                val layoutParams = btnPositive.layoutParams as LinearLayout.LayoutParams
                layoutParams.weight = 10f
                btnPositive.layoutParams = layoutParams
                btnNegative.layoutParams = layoutParams
            }
        }

        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }
}