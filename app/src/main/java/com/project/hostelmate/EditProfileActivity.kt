package com.project.hostelmate

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.project.hostelmate.databinding.ActivityEditProfileBinding
import com.project.hostelmate.preference.Constance
import com.project.hostelmate.preference.PrefsHelper
import com.project.hostelmate.preference.showCustomToastError
import com.project.hostelmate.preference.showCustomToastSucess
import com.project.hostelmate.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        PrefsHelper.init(applicationContext)

        viewProfile()

        binding.submitButtonEditProfile.setOnClickListener {
            validateData()
        }

    }

    private fun viewProfile() {
        binding.progressLayout.visibility = View.VISIBLE
        val userId = PrefsHelper.read(Constance.USER_ID)
        val params = HashMap<String?, String?>()
        params["id"] = userId
        lifecycleScope.launch(Dispatchers.IO) {
            val response = ApiService.invoke().viewUserProfile(params)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val root = response.body()
                    if (root?.status == true) {

                        binding.fullNameEditProfileEt.setText(root.userData.get(0).name.toString())
                        binding.emailIdEditProfileEt.setText(root.userData.get(0).email.toString())
                        binding.phoneNumberEditProfileEt.setText(root.userData.get(0).contactNumber.toString())
                        binding.progressLayout.visibility = View.GONE

                    } else {
                        Toast(applicationContext).showCustomToastError(
                            root?.message.toString(),
                            this@EditProfileActivity
                        )
                        binding.progressLayout.visibility = View.GONE
                    }
                } else {
                    Toast(applicationContext).showCustomToastError(
                        response.message(),
                        this@EditProfileActivity
                    )
                    binding.progressLayout.visibility = View.GONE
                }
            }
        }
    }

    private fun validateData() {
        if (binding.emailIdEditProfileEt.text.isEmpty() || binding.fullNameEditProfileEt.text.isEmpty()
            || binding.phoneNumberEditProfileEt.text.isEmpty()
        ) {
            Toast(applicationContext).showCustomToastError(
                "Fill the details",
                this@EditProfileActivity
            )
        } else {
            editProfileApi()
        }
    }

    private fun editProfileApi() {
        binding.progressLayout.visibility = View.VISIBLE
        val userId = PrefsHelper.read(Constance.USER_ID)
        val name = binding.fullNameEditProfileEt.text.toString()
        val email = binding.emailIdEditProfileEt.text.toString()
        val phone = binding.phoneNumberEditProfileEt.text.toString()
        val params = HashMap<String?, String?>()
        params["userid"] = userId
        params["name"] = name
        params["phone_number"] = phone
        params["email"] = email

        lifecycleScope.launch(Dispatchers.IO) {

            val response = ApiService.invoke().editUserProfile(params)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val root = response.body()

                    if (root?.status == true) {
                        Toast(applicationContext).showCustomToastSucess(
                            root?.message.toString(),
                            this@EditProfileActivity
                        )
                        // binding.progressLayout.visibility = View.GONE
                        viewProfile()

                    } else {
                        Toast(applicationContext).showCustomToastError(
                            root?.message.toString(),
                            this@EditProfileActivity
                        )
                        binding.progressLayout.visibility = View.GONE
                    }


                } else {
                    Toast(applicationContext).showCustomToastError(
                        response.message(),
                        this@EditProfileActivity
                    )
                    binding.progressLayout.visibility = View.GONE

                }
            }

        }

    }
}