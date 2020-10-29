package com.example.fyp1

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp1.databinding.ExampleTimeSlotItemBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class ApptTimeSlotAdapter(
    private val timeSlotList: MutableList<Schedule>,protected var mListener: ItemListener?
) :
    RecyclerView.Adapter<ApptTimeSlotAdapter.ExampleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExampleViewHolder =
        ExampleViewHolder(
            ExampleTimeSlotItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),mListener
        )

    override fun onBindViewHolder(holder: ExampleViewHolder, position: Int) {
        val currentItem = timeSlotList[position]
        holder.bind(currentItem,position)
    }

    override fun getItemCount() = timeSlotList.size

    class ExampleViewHolder(
        private val binding: ExampleTimeSlotItemBinding,
        protected var mListener: ItemListener?
    ) : RecyclerView.ViewHolder(binding.root) {

        var commentDatabaseReference: DatabaseReference? =
            FirebaseDatabase.getInstance().getReference("comment")
        val timeFormat = SimpleDateFormat("HHmm", Locale.getDefault())
        val timeFormatter = SimpleDateFormat("HH:mm")

        fun bind(sch: Schedule, position: Int) {
            val time = timeFormat.parse("${sch.startTime}")
            val timeStr = timeFormatter.format(time)
            binding.time = timeStr
            binding.cardTimeSlot.setOnClickListener {
                //editComments(comments,context,activity) }
                if (mListener != null) {
                    mListener!!.onTimeSlotClick(sch)
                }
                Snackbar.make(it, "$timeStr click", Snackbar.LENGTH_SHORT).show()
            }

        }

    }
    interface ItemListener{
        fun onTimeSlotClick(sch: Schedule)
    }
}