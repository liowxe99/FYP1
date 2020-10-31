package com.example.fyp1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

class DashboardActivity : AppCompatActivity() {

    var tWelcome: TextView? = null
    var cAppt: CardView? = null
    var cDoctor: CardView? = null
    var cComment: CardView? = null
    var cHistory: CardView? = null
    var nMenu: BottomNavigationView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        tWelcome =  findViewById<View>(R.id.textWelcome) as TextView?
        cAppt = findViewById<View>(R.id.cardAppointment) as CardView?
        cDoctor = findViewById<View>(R.id.cardDoctor) as CardView?
        cComment = findViewById<View>(R.id.cardComment) as CardView?
        cHistory = findViewById<View>(R.id.cardHistory) as CardView?
        nMenu = findViewById<View>(R.id.bottomNavigationView) as BottomNavigationView?

        //get data from intent
        val intent = intent
        val userID = intent.getStringExtra("UserID")
        val name = intent.getStringExtra("Name")
        Log.i("myInfoTag","$userID $name")
        tWelcome!!.text = "Hello, $name"
        cAppt!!.setOnClickListener{
            Snackbar.make(it, "Appt click", Snackbar.LENGTH_SHORT).show()
        }
        cDoctor!!.setOnClickListener{
            var intent = Intent(this, DoctorListViewActivity::class.java)

             intent.putExtra("UserID",userID)
             intent.putExtra("Name",name)
            startActivity(intent)
            Snackbar.make(it, "Doctor click", Snackbar.LENGTH_SHORT).show()
        }
        cComment!!.setOnClickListener{
            var intent = Intent(this, CommentsViewAllActivity::class.java)

            intent.putExtra("UserID",userID)
            intent.putExtra("Name",name)
            startActivity(intent)
        }
        cHistory!!.setOnClickListener{
            Snackbar.make(it, "History click", Snackbar.LENGTH_SHORT).show()
        }


        nMenu!!.selectedItemId  = R.id.home

        nMenu!!.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    true
                }
                R.id.appointment -> {
                    true
                }
                R.id.profile -> {
                    val intphto = Intent(this, UserProfileActivity::class.java)
                    intphto.putExtra("UserID",userID)
                    intphto.putExtra("Name",name)
                    startActivity(intphto)
                    overridePendingTransition(0,0)
                    true
                }
            }
            false}
    }
//TODO logout Function
    override fun onBackPressed() {
        super.onBackPressed()

    }
}