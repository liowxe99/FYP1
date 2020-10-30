package com.example.fyp1

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fyp1.databinding.FragmentDoctorListBinding
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_doctor_list.*
import java.lang.Boolean


class doctorListFragment(
    private val name: String,
    private val id: String
) : Fragment() {

    var databaseReference: DatabaseReference? = FirebaseDatabase.getInstance().getReference()
    var staffCount: Int? = 0

    private lateinit var adapter: DoctorsAdapter
    val docList: MutableList<Staff> = mutableListOf<Staff>()
    val docDesc: MutableList<StaffDesc> = mutableListOf<StaffDesc>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentDoctorListBinding>(
            inflater,
            R.layout.fragment_doctor_list, container, false
        )

        adapter = DoctorsAdapter(docList,docDesc, name, id,requireActivity())
        readData()
        return binding.root
    }
//    var prevStarted = "prevStarted"
//    override fun onResume() {
//        super.onResume()
//        val sharedPref = activity?.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
//        if (!sharedPref!!.getBoolean(prevStarted, false)) {
//            readData()
//            val editor = sharedPref.edit()
//            editor.putBoolean(prevStarted, Boolean.TRUE)
//            editor.apply()
//        } else {
//        }
//    }
    private fun readData() {

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    docDesc.clear()
                    docList.clear()

                    val snoList = ArrayList<String>()
                    Log.i("myInfoTag", "connected")
                    val count = dataSnapshot.child("staffCounter").getValue()!!
                    staffCount = count.toString().toInt() + 1
                    Log.i(
                        "myInfoTag",
                        "Current count is $count and next is ${staffCount.toString()}"
                    )

                    for (i in 1 until staffCount.toString().toInt()) {
                        val commentID =
                            dataSnapshot.child("staffSno").child(i.toString()).child("staffID")
                                .getValue()!!
                        //Log.i("myInfoTag", "Comment ID $i : ${commentID.toString()}")
                        snoList.add(commentID.toString())
                        val stf = dataSnapshot.child("staff").child(snoList.get(i - 1))
                            .getValue(Staff::class.java)!!
                        val itemStaff = Staff(
                            stf.staffID.toString(),
                            stf.position.toString(),
                            stf.name.toString(),
                            stf.gender.toString()
                        )
                        if (itemStaff.position == "D") {
                            docList.add(itemStaff)
                            Log.i("myInfoTag", "${stf.staffID.toString()}")
                            val stfDesc =
                                dataSnapshot.child("doctorDesc").child(stf.staffID.toString())
                                    .getValue(StaffDesc::class.java)!!
                            val itemStfDesc = StaffDesc(
                                stfDesc.desc.toString(),
                                stfDesc.expYear.toString().toInt()
                            )
                            docDesc.add(itemStfDesc)

                        }
                        if (!docDesc.isEmpty()){
                        doctor_recycler_view.adapter = adapter
                        doctor_recycler_view.layoutManager =
                            LinearLayoutManager(context)
                        doctor_recycler_view.setHasFixedSize(true)
                        }
                        //Log.i("myInfoTag", "Comment ${comment.commentID.toString()} : ${comment.toString()}")
                        //Log.i("myInfoTag", "${item.toString()}")
                    }

                    //Log.i("myInfoTag", "${adapter.toString()}")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.i("myInfoTag", "loadPost:onCancelled", databaseError.toException())
            }
        }
        databaseReference!!.addListenerForSingleValueEvent(postListener)
    }

}