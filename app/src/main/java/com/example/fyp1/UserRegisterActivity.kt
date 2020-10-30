package com.example.fyp1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.*

class UserRegisterActivity : AppCompatActivity() {
    var tNRIC: String? = null
    var tAddress: String? = null
    var tDateOfBirth: String? = null
    var tEmail: TextInputEditText? = null
    var tGender: String? = null
    var tPassword: TextInputEditText? = null
    var tConfirmPassword: TextInputEditText? = null
    var tName: TextInputEditText? = null
    var tPatientID: String? = null
    var tContact: TextInputEditText? = null
    var tStatus: String? = null
    var tUserID: TextInputEditText? = null

    var btnRegister: AppCompatButton? = null
    var txtLogin: AppCompatTextView? = null

    var patientDatabaseReference: DatabaseReference? = null
    var patientSnoDatabaseReference: DatabaseReference? = null
    var patientCounterDatabaseReference: DatabaseReference? = null
    var patientCount:Int? = 0

    var btnShow: Button? = null
    var ListUser: ListView? = null
    private var result: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_register)
        tEmail = findViewById<View>(R.id.textInputEditTextEmail) as TextInputEditText
        tPassword= findViewById<View>(R.id.textInputEditTextPassword) as TextInputEditText
        tConfirmPassword= findViewById<View>(R.id.textInputEditTextConfirmPassword) as TextInputEditText
        tName = findViewById<View>(R.id.textInputEditTextName) as TextInputEditText
        tContact = findViewById<View>(R.id.textInputEditTextContactNum) as TextInputEditText
        tUserID= findViewById<View>(R.id.textInputEditTextUserID) as TextInputEditText

        tNRIC = "null"
        tAddress = "null"
        tDateOfBirth = "null"
        tGender = "null"
        tPatientID = "null"
        tStatus = "null"

        btnRegister = findViewById<View>(R.id.appCompatButtonRegister) as AppCompatButton
        txtLogin = findViewById<View>(R.id.appCompatTextViewLoginLink) as AppCompatTextView

        patientDatabaseReference = FirebaseDatabase.getInstance().getReference("patient")
        patientSnoDatabaseReference = FirebaseDatabase.getInstance().getReference("patientSno")
        patientCounterDatabaseReference = FirebaseDatabase.getInstance().getReference("patientCounter")

        btnRegister!!.setOnClickListener {
            addArrayList()
//            val intphto = Intent(applicationContext, UserLoginActivity::class.java)
//            startActivity(intphto)
        }
        txtLogin!!.setOnClickListener {
            val intphto = Intent(applicationContext, UserLoginActivity::class.java)
            startActivity(intphto)
        }
    }

    private fun addArrayList() {

        val ID = tUserID!!.text.toString().trim { it <= ' ' }
        val name = tName!!.text.toString().trim { it <= ' ' }
        val email = tEmail!!.text.toString().trim { it <= ' ' }
        val password = tPassword!!.text.toString().trim { it <= ' ' }
        val comfirmpassword = tConfirmPassword!!.text.toString().trim { it <= ' ' }
        val contact= tContact!!.text.toString().trim{it <= ' '}
        if (TextUtils.isEmpty(ID)) {
            tUserID!!.error = "Please enter your ID!"
        } else if (TextUtils.isEmpty(name)) {
            tName!!.error = "Please enter your Name!"
        } else if (TextUtils.isEmpty(email)) {
            tEmail!!.error = "Please enter your Email!"
        } else if (TextUtils.isEmpty(password)) {
            tPassword!!.error = "Please enter your Password!"
        } else if (password != comfirmpassword) {
            tConfirmPassword!!.error = "Please put the same password"
        } else if(TextUtils.isEmpty(contact)){
            tContact!!.error = "Please enter your Contact Number"
        }else {

            //String id=  databaseReference.push().getKey();
            val patient = Patients(
                tNRIC,
                tAddress,
                tDateOfBirth,
                email,
                tGender,
                Security.encrypt(password),
                name,
                tPatientID,
                contact,
                tStatus,
                ID
            )
            patientCounterDatabaseReference!!.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val count = dataSnapshot.getValue()!!
                        patientCount = count.toString().toInt() + 1
                          Log.i("myInfoTag","Current count is $count and next is ${patientCount.toString()}")
                        patientCounterDatabaseReference!!.setValue(patientCount.toString())
                        patientSnoDatabaseReference!!.child(patientCount.toString()).child("userID").setValue(ID)
                        patientDatabaseReference!!.child(ID).setValue(patient)
                    }
                }
            })
//            patientDatabaseReference!!.child(ID).child("NRIC").setValue(tNRIC)
//            patientDatabaseReference!!.child(ID).child("address").setValue(tAddress)
//            patientDatabaseReference!!.child(ID).child("dateOfBirth").setValue(tDateOfBirth)
//            patientDatabaseReference!!.child(ID).child("email").setValue(email)
//            patientDatabaseReference!!.child(ID).child("gender").setValue(tGender)
//            try {
//                patientDatabaseReference!!.child(ID).child("loginPw").setValue(Security.encrypt(password))
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//            patientDatabaseReference!!.child(ID).child("name").setValue(name)
//            patientDatabaseReference!!.child(ID).child("patientID").setValue(tPatientID)
//            patientDatabaseReference!!.child(ID).child("phoneNo").setValue(contact)
//            patientDatabaseReference!!.child(ID).child("status").setValue(tStatus)
//            patientDatabaseReference!!.child(ID).child("userID").setValue(ID)

            Toast.makeText(this, "Register Success", Toast.LENGTH_LONG).show()
            Cleartxt()
        }
    }

    private fun Cleartxt() {
        tUserID!!.setText("")
        tEmail!!.setText("")
        tName!!.setText("")
        tPassword!!.setText("")
        tConfirmPassword!!.setText("")
        tContact!!.setText("")
        tUserID!!.requestFocus()
    }

}