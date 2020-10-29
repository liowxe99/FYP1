package com.example.fyp1

import android.os.Bundle
import android.util.Log
import android.widget.CalendarView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fyp1.databinding.ActivityApptBookingBinding
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_appt_booking.*
import java.sql.Date
import java.text.SimpleDateFormat
import androidx.databinding.DataBindingUtil

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
        selectedDate = dateformat.format(Date(calendarView.date))
        binding.date = "${getString(R.string.apptDate)}${dateformatter.format(dateformat.parse(selectedDate))}"
        binding.time = "${getString(R.string.apptTime)} none"

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            selectedDate = "$year${month+1}$dayOfMonth"
            readData()
            binding.date = "${getString(R.string.apptDate)}${dateformatter.format(dateformat.parse(selectedDate))}"
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
                        if (staffID == sch.staffID.toString()&&selectedDate==sch.schDate.toString()){
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

    private fun updateApptTimeText(
        sch: Schedule) {

    }

    override fun onTimeSlotClick(sch: Schedule) {
        val timeFormat = SimpleDateFormat("HHmm")
        val timeFormatter = SimpleDateFormat("HH:mm")

        val time = timeFormat.parse("${sch.startTime}")
        val timeStr = timeFormatter.format(time)

        binding.time = "${getString(R.string.apptTime)}$timeStr"

    }
}