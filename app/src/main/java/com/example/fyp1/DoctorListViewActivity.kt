package com.example.fyp1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class DoctorListViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_list_view)

        val intent = intent
        val userID = intent.getStringExtra("UserID")
        val name = intent.getStringExtra("Name")

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, doctorListFragment("$name","$userID"))
        transaction.disallowAddToBackStack()
        transaction.commit()

    }

}