package com.example.fyp1

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp1.databinding.ExampleDoctorItemBinding
import kotlinx.android.synthetic.main.fragment_doctor_list.*

class DoctorsAdapter (private val docList:MutableList<Staff>,private val docDesc:MutableList<StaffDesc>, private  val name:String,private val id:String,private val activity:FragmentActivity):RecyclerView.Adapter<DoctorsAdapter.ExampleViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorsAdapter.ExampleViewHolder = ExampleViewHolder(
        ExampleDoctorItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ),name,id,activity
    )
    override fun onBindViewHolder(holder: ExampleViewHolder, position: Int) {
        Log.i("myInfoTag","$position ${docDesc.size}" )
        val currentStaff = docList[position]
        val currentStaffDesc = docDesc[position]

        holder.bind(currentStaff,currentStaffDesc)
    }

    override fun getItemCount() = docList.size

    class ExampleViewHolder(private val binding: ExampleDoctorItemBinding, private  val name:String,private val id:String,private val activity:FragmentActivity) : RecyclerView.ViewHolder(binding.root) {

        fun bind(stf: Staff, stfDesc: StaffDesc) {

            binding.name = stf.name
            //binding.deleteComm.setImageResource(R.drawable.ic_edit)
            binding.desc = stfDesc.desc
            if (stf.gender == "M") {
                binding.doctImage.setImageResource(R.drawable.ic_doctor_men)
            } else {
                binding.doctImage.setImageResource(R.drawable.ic_doctor_women)
            }

            binding.cardDoctor.setOnClickListener {
                //Snackbar.make(it, "$patientID click", Snackbar.LENGTH_SHORT).show()
                val transaction = activity?.supportFragmentManager?.beginTransaction()
                transaction?.replace(R.id.frame_layout, doctorDetailsFragment(name,id,stf,stfDesc))
                transaction?.disallowAddToBackStack()
                transaction?.commit()
            }
        }
    }
}