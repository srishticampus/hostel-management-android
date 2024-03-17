package com.project.hostelmate.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.project.hostelmate.EditProfileActivity
import com.project.hostelmate.R
import com.project.hostelmate.ViewOutPassActivity
import com.project.hostelmate.ViewVisitorPassActivity
import com.project.hostelmate.databinding.FragmentProfileBinding
import com.project.hostelmate.preference.Constance
import com.project.hostelmate.preference.PrefsHelper
import com.project.hostelmate.preference.showCustomToastError
import com.project.hostelmate.preference.showCustomToastSucess
import com.project.hostelmate.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        PrefsHelper.init(requireContext())

        binding.viewProfileTv.setOnClickListener {
            startActivity(Intent(context, EditProfileActivity::class.java))
        }
        binding.viewOutPassTv.setOnClickListener {
            startActivity(Intent(context, ViewOutPassActivity::class.java))
        }
        binding.viewVisitorPassTv.setOnClickListener {
            startActivity(Intent(context, ViewVisitorPassActivity::class.java))
        }

        viewMessPref()

        binding.messRadGroup.setOnCheckedChangeListener { group, checkedId ->
            val radioButton = view.findViewById<RadioButton>(checkedId)

            if (radioButton != null) {
                val pref = when (radioButton.id) {
                    R.id.radio_btn_veg -> changeMessPref("Veg")
                    R.id.radio_btn_non_veg -> changeMessPref("Non Veg")
                    R.id.radio_btn_no_food -> changeMessPref("no food")
                    else -> {
                        Toast.makeText(requireContext(), "Failed to change", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

        }



        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun changeMessPref(pref: String) {
        binding.progressLayout.visibility = View.VISIBLE
        val userId = PrefsHelper.read(Constance.USER_ID)
        val params = HashMap<String?, String?>()
        params["userid"] = userId
        params["messpreference"] = pref

        lifecycleScope.launch(Dispatchers.IO) {
            val response = ApiService.invoke().changeMessPref(params)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val root = response.body()
                    if (root?.status == true) {
                        Toast(requireContext()).showCustomToastSucess(
                            root?.message.toString(),
                            requireActivity()
                        )
                        binding.progressLayout.visibility = View.GONE
                    } else {
                        Toast(requireContext()).showCustomToastError(
                            root?.message.toString(),
                            requireActivity()
                        )
                        binding.progressLayout.visibility = View.GONE
                    }
                } else {
                    Toast(requireContext()).showCustomToastError(
                        response.message(),
                        requireActivity()
                    )
                    binding.progressLayout.visibility = View.GONE
                }
            }
        }


    }

    private fun viewMessPref() {
        binding.progressLayout.visibility = View.VISIBLE
        val userId = PrefsHelper.read(Constance.USER_ID)
        val params = HashMap<String?, String?>()
        params["userid"] = userId

        lifecycleScope.launch(Dispatchers.IO) {
            val response = ApiService.invoke().viewMessPref(params)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val root = response.body()
                    if (root?.status == true) {
                        binding.progressLayout.visibility = View.GONE

                        when (root?.details?.messPreference.toString()) {
                            "Veg" -> binding.radioBtnVeg.isChecked = true
                            "Non Veg" -> binding.radioBtnNonVeg.isChecked = true
                            "no food" -> binding.radioBtnNoFood.isChecked = true
                            else -> Toast.makeText(
                                requireContext(),
                                "Pref Error",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    } else {
                        Toast(requireContext()).showCustomToastError(
                            root?.message.toString(),
                            requireActivity()
                        )
                        binding.progressLayout.visibility = View.GONE
                    }
                } else {
                    Toast(requireContext()).showCustomToastError(
                        response.message(),
                        requireActivity()
                    )
                    binding.progressLayout.visibility = View.GONE
                }
            }
        }

    }


}