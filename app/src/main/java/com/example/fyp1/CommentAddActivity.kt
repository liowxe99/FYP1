package com.example.fyp1

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_comment_add.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CommentAddActivity : AppCompatActivity() {

    var commentDatabaseReference: DatabaseReference? = FirebaseDatabase.getInstance().getReference()

    var commentCount: Int? = 0

    lateinit var userID:String
    lateinit var name:String
    lateinit var tRatingBar: RatingBar
    lateinit var tRate: TextView
    lateinit var tComm: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment_add)
        tRatingBar = findViewById<RatingBar>(R.id.rateBar)
        tRate = findViewById<TextView>(R.id.rateText)
        tComm = findViewById<EditText>(R.id.commentsEditText)

        val intent = intent
        userID = intent.getStringExtra("UserID")
        name = intent.getStringExtra("Name")

        rateBar.setOnRatingBarChangeListener(object : RatingBar.OnRatingBarChangeListener {
            override fun onRatingChanged(p0: RatingBar?, p1: Float, p2: Boolean) {
                //Toast.makeText(this@CommentAddActivity, "Given rating is: $p1", Toast.LENGTH_SHORT).show()
                tRate.text = p1.toString()
            }
        })
        commentsEditText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                textCounter.text = "${s.length}/100"
            }
        })
//
//        var navigationView = findViewById<BottomNavigationView>(R.id.navigationView)
//
//        navigationView.selectedItemId  = R.id.delete
//
//        navigationView.setOnNavigationItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.edit -> {
//                    val intphto = Intent(this, CommentsViewActivity::class.java)
//                    startActivity(intphto)
//                    overridePendingTransition(0,0)
//
//                    Toast.makeText(this,"edit click!", Toast.LENGTH_SHORT)
//                    true
//                }
//                R.id.delete -> {
//                    true
//                }
//            }
//            false}


}
    fun backFunction(view: View){
        val intent = Intent(this, CommentsViewActivity::class.java)
        intent.putExtra("UserID",userID)
        intent.putExtra("Name",name)
        startActivity(intent)
    }
    fun generateID(lastID:String):String{
        var num = lastID.substring(1).toInt()
        return "C"+String.format("%04d",num+1)
    }

    fun insertItem(view: View) {
        var commDate: String
        var commTime: String
        val content = commentsEditText.text.toString().trim{it<=' '}
        var commentID: String
        val patientID: String = userID
        var rating: String = rateText.text.toString()
        var status: String = "enabled"

        if (TextUtils.isEmpty(content)) {
            commentsEditText.error = "Please enter the comment content!"
        }else {

            val postListener = object :  ValueEventListener{
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val snoList = ArrayList<String>()
                        Log.i("myInfoTag", "connected")
                        val count = dataSnapshot.child("commentsCounter").getValue()!!
                        commentCount = count.toString().toInt() + 1
                        Log.i("myInfoTag", "Current count is $count and next is ${commentCount.toString()}")
                        val id = dataSnapshot.child("commentsSno").child(count.toString()).child("commentID").getValue()!!

                        Log.i("myInfoTag","ID is : "+generateID(id.toString()))
                        commentID = generateID(id.toString())

                        val alertDialog = AlertDialog.Builder(this@CommentAddActivity,R.style.AlertDialogCustom).create()
                        alertDialog.setTitle("Confirmation")
                        alertDialog.setMessage("Do you want to post this comment?")

                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Confirm") { dialog, which ->
                            commDate = SimpleDateFormat("yyyyMMdd",Locale.getDefault()).format(Date())
                            commTime =SimpleDateFormat("HH:mm:ss",Locale.getDefault()).format(Date())
                            val comments = Comments(commDate,commTime,content,commentID,patientID,rating,status)

                            Toast.makeText(applicationContext,"Confirm", Toast.LENGTH_SHORT).show()
                            Log.i("myInfoTag", "${comments.toString()}")
                            commentDatabaseReference!!.child("comment").child(commentID).setValue(comments)
                            commentDatabaseReference!!.child("commentsSno").child(commentCount.toString()).child("commentID").setValue(commentID)
                            commentDatabaseReference!!.child("commentsCounter").setValue(commentCount.toString())

                            tComm.text.clear()
                            tComm.requestFocus()
                            Log.i("myInfoTag", "completed!!") }

                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel") { dialog, which ->
                            Toast.makeText(applicationContext,
                                android.R.string.cancel, Toast.LENGTH_SHORT).show()
                        }
                        alertDialog.show()


                        val btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        val btnNegative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)

                        val layoutParams = btnPositive.layoutParams as LinearLayout.LayoutParams
                        layoutParams.weight = 10f
                        btnPositive.layoutParams = layoutParams
                        btnNegative.layoutParams = layoutParams
                        btnNegative.setTextColor(getResources().getColor(R.color.colorDialogCancel))

                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.i("myInfoTag", "loadPost:onCancelled", databaseError.toException())
                }
            }
            commentDatabaseReference!!.addListenerForSingleValueEvent(postListener)
        }
    }
    public fun hihi(){

    }
}