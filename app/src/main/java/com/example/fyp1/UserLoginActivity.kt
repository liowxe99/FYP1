package com.example.fyp1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import com.example.fyp1.Utils.AppPreferences
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.*

class UserLoginActivity : AppCompatActivity() {
    var tUserID: TextInputEditText? = null
    var tPassword: TextInputEditText? = null
    var btnLogin: AppCompatButton? = null
    var txtRegister: AppCompatTextView? = null
    var databaseReference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_login)
        if (AppPreferences.isLogin) {
            val intent = Intent(applicationContext, DashboardActivity::class.java)
            intent.putExtra("UserID", AppPreferences.userid)
            intent.putExtra("Name", AppPreferences.username)
            startActivity(intent)
        }else{
            tUserID = findViewById<View>(R.id.textInputEditTextUserID) as TextInputEditText?
            tPassword = findViewById<View>(R.id.textInputEditTextPassword) as TextInputEditText?
            btnLogin = findViewById<View>(R.id.appCompatButtonLogin) as AppCompatButton?
            txtRegister = findViewById<View>(R.id.textViewLinkRegister) as AppCompatTextView?
            databaseReference = FirebaseDatabase.getInstance().getReference("patient")
            btnLogin!!.setOnClickListener {
                //pw encryption
                var pwd1: String? = null
                try {
                    pwd1 = Security.encrypt(tPassword!!.text.toString())

                } catch (e: Exception) {
                    e.printStackTrace()
                }
                Log.i("myInfoTag","info message:"+pwd1.toString())
                logIn(tUserID!!.text.toString(), tPassword!!.text.toString())
            }
            txtRegister!!.setOnClickListener {
                val intphto = Intent(applicationContext, UserRegisterActivity::class.java)
                startActivity(intphto)
            }
        }
    }

    private fun logIn(id: String, password: String) {
        databaseReference!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.child(id).exists()) {
                    if (!id.isEmpty()) {
                        val patient = dataSnapshot.child(id).getValue(Patients::class.java)!!
                        var pwd: String? = null
                        try {
                            pwd = Security.decrypt(patient.loginPw.toString())
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        //Toast.makeText(this@UserLoginActivity, user.loginPw.toString(), Toast.LENGTH_LONG).show()
                        if (pwd == password) {
                            AppPreferences.isLogin = true
                            AppPreferences.username = patient.name.toString()
                            AppPreferences.userid = patient.userID.toString()
                            Toast.makeText(this@UserLoginActivity, "Login Success", Toast.LENGTH_LONG).show()
                            val intent = Intent(applicationContext, DashboardActivity::class.java)
                            intent.putExtra("UserID", AppPreferences.userid)
                            intent.putExtra("Name", AppPreferences.username)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@UserLoginActivity, "Password Incorrect", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this@UserLoginActivity, "User is not register", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this@UserLoginActivity, "User is not register", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}