package com.project.hostelmate.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.project.hostelmate.AttendanceActivity
import com.project.hostelmate.OutPassActivity
import com.project.hostelmate.VisitorPassActivity
import com.project.hostelmate.databinding.FragmentHomeBinding
import com.project.hostelmate.preference.Constance
import com.project.hostelmate.preference.PrefsHelper


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root


        PrefsHelper.init(requireContext())

        if (PrefsHelper.read(Constance.HOSTEL_TYPE)?.equals("College") == true) {
            binding.attendanceBt.visibility = View.VISIBLE
        } else {
            binding.attendanceBt.visibility = View.GONE
        }

        val userName = PrefsHelper.read(Constance.USER_NAME)
        binding.userNameTv.text = userName.toString()

        binding.attendanceBt.setOnClickListener {
            startActivity(Intent(context, AttendanceActivity::class.java))
        }
        binding.outpassBt.setOnClickListener {
            startActivity(Intent(context, OutPassActivity::class.java))
        }
        binding.visitorPassBt.setOnClickListener {
            startActivity(Intent(context, VisitorPassActivity::class.java))
        }

        return view
    }


}