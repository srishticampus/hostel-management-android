package com.project.hostelmate.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.hostelmate.adapter.ChatAdapter
import com.project.hostelmate.databinding.FragmentChatBinding
import com.project.hostelmate.preference.Constance
import com.project.hostelmate.preference.PrefsHelper
import com.project.hostelmate.preference.showCustomToastError
import com.project.hostelmate.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ChatFragment : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        val view = binding.root

        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        PrefsHelper.init(requireContext())


        viewAllChat()

        binding.sendButton.setOnClickListener {
            if (binding.messageEditText.text.isEmpty()){

            }else{
                sendMessage()
            }
        }


        return view
    }

    private fun viewAllChat() {
        val userId = PrefsHelper.read(Constance.USER_ID)
        val hostelId = PrefsHelper.read(Constance.HOSTEL_ID)

        //Toast.makeText(context, userId.toString(), Toast.LENGTH_SHORT).show()

        val params = HashMap<String?, String?>()
        params["hostal_id"] = hostelId
        params["userid"] = userId
        lifecycleScope.launch(Dispatchers.IO) {
            val response = ApiService.invoke().viewAllChat(params)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val root = response.body()
                    if (root?.status == true) {

                        val layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        binding.messageRecyclerView.layoutManager = layoutManager
                        val adapter = ChatAdapter(root, requireContext(), userId.toString())
                        binding.messageRecyclerView.adapter = adapter


                    } else {
                        Toast(context).showCustomToastError(
                            root?.message.toString(),
                            requireActivity()
                        )
                        // binding.progressLayout.visibility = View.GONE
                    }
                } else {
                    Toast(context).showCustomToastError(
                        response.message(),
                        requireActivity()
                    )
                    // binding.progressLayout.visibility = View.GONE
                }
            }
        }


    }

    private fun sendMessage(){
        val userId = PrefsHelper.read(Constance.USER_ID)
        val hostelId = PrefsHelper.read(Constance.HOSTEL_ID)
        val params = HashMap<String?, String?>()
        params["hostal_id"] = hostelId
        params["userid"] = userId
        params["message"] = binding.messageEditText.text.toString()


        lifecycleScope.launch (Dispatchers.IO){
            val response = ApiService.invoke().sentMessage(params)
            withContext(Dispatchers.Main){
                if (response.isSuccessful){
                    val root = response.body()
                    if (root?.status == true) {

                        viewAllChat()
                        binding.messageEditText.text.clear()

                    } else {
                        Toast(context).showCustomToastError(
                            root?.message.toString(),
                            requireActivity()
                        )
                    }

                }else{
                    Toast(context).showCustomToastError(
                        response.message(),
                        requireActivity()
                    )
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}