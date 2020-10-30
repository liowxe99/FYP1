package com.example.fyp1

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fyp1.databinding.ActivityApptBookingBinding
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_appt_booking.*
import java.sql.Date
import java.text.SimpleDateFormat
import androidx.databinding.DataBindingUtil
import kotlinx.android.synthetic.main.activity_comment_add.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.time.minutes

class ApptBookingActivity : AppCompatActivity(),ApptTimeSlotAdapter.ItemListener {

    var databaseReference: DatabaseReference? = FirebaseDatabase.getInstance().getReference()
    var schCount:Int? = 0

//    private lateinit var deleteIcon: Drawable
//    private lateinit var editIcon: Drawable
//    private var swipeBgdEdit: ColorDrawable = ColorDrawable(Color.parseColor("#0000FF"))
//    private var swipeBgdDelete: ColorDrawable = ColorDrawable(Color.parseColor("#0000FF"))

    var gridLayoutManager:GridLayoutManager? = null
    private lateinit var adapter: ApptTimeSlotAdapter
    val timeSlotList: MutableList<Schedule> = mutableListOf<Schedule>()
    private lateinit var binding: ActivityApptBookingBinding
    lateinit var userID:String
    lateinit var staffID:String
    lateinit var name:String
    lateinit var docName:String
    lateinit var selectedDate:String
    val dateformat = SimpleDateFormat("yyyyMMdd")
    val dateformatter = SimpleDateFormat("yyyy-MM-dd")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = ApptTimeSlotAdapter(timeSlotList,this)
        readData()

        setContentView(R.layout.activity_appt_booking)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_appt_booking)
        val intent = intent
        userID = intent.getStringExtra("UserID")
        staffID = intent.getStringExtra("StaffID")
        name = intent.getStringExtra("Name")
        docName = intent.getStringExtra("docName")
        selectedDate = dateformat.format(Date(calendarView.date))
        binding.date = "${getString(R.string.apptDate)}${dateformatter.format(dateformat.parse(selectedDate))}"
        binding.apptDate = dateformatter.format(dateformat.parse(selectedDate))
        binding.time = "${getString(R.string.apptTime)} none"

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            selectedDate = "$year${month+1}$dayOfMonth"
            readData()
            binding.date = "${getString(R.string.apptDate)}${dateformatter.format(dateformat.parse(selectedDate))}"
            binding.apptDate = dateformatter.format(dateformat.parse(selectedDate))
            binding.time = "${getString(R.string.apptTime)} none"
            binding.apptTime=null
        }
    }
    private fun readData(){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    timeSlotList.clear()
                    val snoList = ArrayList<String>()
                    Log.i("myInfoTag", "connected")
                    val count = dataSnapshot.child("scheduleCounter").getValue()!!
                    schCount = count.toString().toInt() + 1
                    Log.i("myInfoTag", "Current count is $count and next is ${schCount.toString()}")

                    for (i in 1 until schCount.toString().toInt()) {
                        val commentID = dataSnapshot.child("scheduleSno").child(i.toString()).child("scheduleID").getValue()!!
                        Log.i("myInfoTag", "Comment ID $i : ${commentID.toString()}")
                        snoList.add(commentID.toString())
                        val sch = dataSnapshot.child("schedule").child(snoList.get(i - 1)).getValue(Schedule::class.java)!!

//                        val comment = dataSnapshot.child("comment").child(snoList.get(i - 1))
//                            .getValue(Comments::class.java)!!
                        val item = Schedule(
                            sch.endTime.toString(),
                            sch.schDate.toString(),
                            sch.scheduleID.toString(),
                            sch.staffID.toString(),
                            sch.startTime.toString(),
                            sch.status.toString()
                        )
//                        if (item.status!="disabled" )
                            //timeSlotList.add(item)
                        if (staffID == sch.staffID.toString()&&selectedDate==sch.schDate.toString()&&sch.status=="A"){
                        timeSlotList.add(item)
                        }
                        //Log.i("myInfoTag", "Comment ${comment.commentID.toString()} : ${comment.toString()}")
                        //Log.i("myInfoTag", "${item.toString()}")
                    }

                    //Log.i("myInfoTag", "${adapter.toString()}")
                    gridLayoutManager= GridLayoutManager(applicationContext,3,LinearLayoutManager.VERTICAL,false)
                    val layoutManager = AutoFitGridLayoutManager(applicationContext, 289)
                    recycler_view.layoutManager = layoutManager
                    recycler_view.setHasFixedSize(true)

                    recycler_view.adapter = adapter
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.i("myInfoTag", "loadPost:onCancelled", databaseError.toException())
            }
        }
        databaseReference!!.addValueEventListener(postListener)
    }
    fun getRandomString(length: Int) : String {
        val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')

        return List(length) { charset.random() }
            .joinToString("")
    }
    fun insertAppt(view:View){
        val apptID = getRandomString(10)
        val apptTime =binding.apptTime.toString().trim{it<=' '}
        val apptDateStr = binding.apptDate.toString().trim{it<=' '}
        var apptDate:String = dateformat.format(dateformatter.parse(apptDateStr))
        val scheduleID = binding.schID.toString()
        val patientID = userID
        val apptStatus = "Incoming"

        println("appt time  $apptTime")
        if (apptTime=="null") {
            Toast.makeText(this,"Please enter the available time slot!",Toast.LENGTH_LONG).show()
        }else{

            val postListener = object :  ValueEventListener{
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val snoList = ArrayList<String>()
                        Log.i("myInfoTag", "connected")
//                        val count = dataSnapshot.child("commentsCounter").getValue()!!
//                        if ()
//                        commentCount = count.toString().toInt() + 1
//                        Log.i("myInfoTag", "Current count is $count and next is ${commentCount.toString()}")
//                        val id = dataSnapshot.child("commentsSno").child(count.toString()).child("commentID").getValue()!!
//
//                        Log.i("myInfoTag","ID is : "+generateID(id.toString()))
//                        commentID = generateID(id.toString())

                        val alertDialog = AlertDialog.Builder(this@ApptBookingActivity,R.style.AlertDialogCustom).create()
                        alertDialog.setTitle("Confirmation")
                        val msg = "Patient Name: $name\nDoctor Name: $docName\n${binding.date}\n${binding.time}"
                        alertDialog.setMessage("Confirm the appointment details\n\n$msg")

                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Confirm") { dialog, which ->

                            val appt = Appointment(apptID,apptTime,apptDate,scheduleID,patientID,apptStatus)

                            Toast.makeText(applicationContext,"Confirm", Toast.LENGTH_SHORT).show()
                            Log.i("myInfoTag", "${appt.toString()}")
//                            databaseReference!!.child("commentsSno").child(commentCount.toString()).child("commentID").setValue(commentID)
//                            databaseReference!!.child("commentsCounter").setValue(commentCount.toString())
                            databaseReference!!.child("schedule").child(scheduleID).child("status").setValue("U")
                            databaseReference!!.child("appointment").child(apptID).setValue(appt)


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
            databaseReference!!.addListenerForSingleValueEvent(postListener)
            //println(apptDate)
        }

    }

    fun isDuplicate(){
        //TODO check apptID isNotDuplicate
    }
    override fun onTimeSlotClick(sch: Schedule) {
        val timeFormat = SimpleDateFormat("HHmm")
        val timeFormatter = SimpleDateFormat("HH:mm")

        val time = timeFormat.parse("${sch.startTime}")
        val timeStr = timeFormatter.format(time)
        val endTime =  timeFormat.parse("${sch.endTime}")
        val endTimeStr = timeFormatter.format(endTime)

        binding.schID = sch.scheduleID
        //println("$timeStr - $endTimeStr")
        binding.apptTime = "$timeStr"
        binding.time = "${getString(R.string.apptTime)}$timeStr - $endTimeStr"

    }
}