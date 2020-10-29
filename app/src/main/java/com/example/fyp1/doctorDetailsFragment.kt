package com.example.fyp1

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.fyp1.databinding.FragmentDoctorDetailsBinding

class doctorDetailsFragment(
    private val name: String,
    private val id: String,
    private val stf: Staff,
    private val stfDesc: StaffDesc
) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentDoctorDetailsBinding>(
            inflater,
            R.layout.fragment_doctor_details, container, false
        )

        if (stf.gender.toString() == "M") {
            binding.imageView.setImageResource(R.drawable.ic_doctor_men)
        } else {
            binding.imageView.setImageResource(R.drawable.ic_doctor_women)
        }

        binding.name = stf.name
        binding.desc = stfDesc.desc
        binding.expYear = "${stfDesc.expYear.toString()} Year(s) Experience"
        binding.btnBack.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frame_layout, doctorListFragment(name,id))
            transaction.disallowAddToBackStack()
            transaction.commit()
        }
        binding.btnAppt.setOnClickListener{
            val intent = Intent(context, ApptBookingActivity::class.java)
            intent.putExtra("UserID",id)
            intent.putExtra("StaffID",stf.staffID.toString())
            startActivity(intent)
        }
        return binding.root
    }
}