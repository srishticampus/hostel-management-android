package com.project.hostelmate

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.project.hostelmate.databinding.ActivitySignUpMainBinding
import com.project.hostelmate.preference.Constance
import com.project.hostelmate.preference.PrefsHelper
import com.project.hostelmate.preference.showCustomToastError
import com.project.hostelmate.preference.showCustomToastSucess
import com.project.hostelmate.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SignUpMain : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpMainBinding
    private var messPref = "no food"
    private var roomPref = "Sharing"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        PrefsHelper.init(applicationContext)

        val hostelType = PrefsHelper.read(Constance.HOSTEL_TYPE)

        binding.noFoodRadBtn.isChecked = true
        binding.sharingRadBtn.isChecked = true

        if (hostelType.equals("College")) {

        } else {
            binding.batchNameLayoutText.visibility = View.GONE
            binding.batchNameLayout.visibility = View.GONE
        }

        binding.messPrefRadGroup.setOnCheckedChangeListener { group, checkedId ->
            val radioButton = findViewById<RadioButton>(checkedId)
            if (radioButton != null) {
                messPref = when (radioButton.id) {
                    R.id.radio_btn_veg -> "Veg"
                    R.id.radio_btn_no_food -> "no food"
                    R.id.non_veg_rad_btn -> "Non Veg"
                    else -> ({
                        Toast.makeText(this@SignUpMain, "Failed to change", Toast.LENGTH_SHORT)
                            .show()
                    }).toString()
                }
            }

        }

        binding.roomPrefRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            val radioButton = findViewById<RadioButton>(checkedId)
            if (radioButton != null) {
                roomPref = when (radioButton.id) {
                    R.id.sharing_rad_btn -> "Sharing"
                    R.id.single_room_rad_btn -> "Single"
                    else -> ({
                        Toast.makeText(this@SignUpMain, "Failed to change", Toast.LENGTH_SHORT)
                            .show()
                    }).toString()
                }
            }

        }

        binding.addressEt.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                if ((v as EditText).lineCount >= 3) return@OnKeyListener true
            }
            false
        })

        binding.signUpButton.setOnClickListener {

            if (hostelType?.equals("College") == true) {

                validateCollegeRegData(roomPref, messPref)

            } else {
                validatePrivateHostelRegData(roomPref, messPref)
            }
        }


    }

    private fun validateCollegeRegData(roomPref: String, messPref: String) {
        if (binding.nameEt.text.isNotEmpty() &&
            binding.phoneEt.text.isNotEmpty() &&
            binding.emailEt.text.isNotEmpty() &&
            binding.passwordEt.text.isNotEmpty() &&
            binding.retypeEt.text.isNotEmpty() &&
            binding.addressEt.text.isNotEmpty() &&
            binding.batchNameEt.text.isNotEmpty()
        ) {

            if (binding.passwordEt.text.toString().equals(binding.retypeEt.text.toString())) {


                collegeHostelReg(roomPref, messPref)

            } else {
                Toast(applicationContext).showCustomToastError(
                    "Passwords not match",
                    this@SignUpMain
                )
            }


        } else {
            Toast(applicationContext).showCustomToastError(
                "Fill all data",
                this@SignUpMain
            )
        }
    }

    private fun validatePrivateHostelRegData(roomPref: String, messPref: String) {
        if (binding.nameEt.text.isNotEmpty() &&
            binding.phoneEt.text.isNotEmpty() &&
            binding.emailEt.text.isNotEmpty() &&
            binding.passwordEt.text.isNotEmpty() &&
            binding.retypeEt.text.isNotEmpty() &&
            binding.addressEt.text.isNotEmpty()
        ) {

            if (binding.passwordEt.text.toString().equals(binding.retypeEt.text.toString())) {


                privateHostelReg(roomPref, messPref)

            } else {
                Toast(applicationContext).showCustomToastError(
                    "Passwords not match",
                    this@SignUpMain
                )
            }


        } else {
            Toast(applicationContext).showCustomToastError(
                "Fill all data",
                this@SignUpMain
            )
        }
    }

    private fun collegeHostelReg(roomPref: String, messPref: String) {

        val hostelId = PrefsHelper.read(Constance.HOSTEL_ID)
        val hostelType = PrefsHelper.read(Constance.HOSTEL_TYPE)
        val params = HashMap<String?, String?>()
        params["name"] = binding.nameEt.text.toString()
        params["phone_number"] = binding.phoneEt.text.toString()
        params["email"] = binding.emailEt.text.toString()
        params["address"] = binding.addressEt.text.toString()
        params["batch_name"] = binding.batchNameEt.text.toString()
        params["password"] = binding.retypeEt.text.toString()
        params["hostal_id"] = hostelId
        params["mess_preference"] = messPref
        params["room_preference"] = roomPref

        lifecycleScope.launch(Dispatchers.IO) {
            val response = ApiService.invoke().collegeHostelReg(params)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val root = response.body()
                    if (root?.status == true) {

                        Toast(applicationContext).showCustomToastSucess(
                            root?.message.toString(),
                            this@SignUpMain
                        )
                        startActivity(Intent(this@SignUpMain, MainActivity::class.java))


                    } else {
                        Toast(applicationContext).showCustomToastError(
                            root?.message.toString(),
                            this@SignUpMain
                        )
                    }
                } else {
                    Toast(applicationContext).showCustomToastError(
                        response.message(),
                        this@SignUpMain
                    )
                }
            }

        }


    }

    private fun privateHostelReg(roomPref: String, messPref: String) {

        val hostelId = PrefsHelper.read(Constance.HOSTEL_ID)
        val hostelType = PrefsHelper.read(Constance.HOSTEL_TYPE)
        val params = HashMap<String?, String?>()
        params["name"] = binding.nameEt.text.toString()
        params["phone_number"] = binding.phoneEt.text.toString()
        params["email"] = binding.emailEt.text.toString()
        params["address"] = binding.addressEt.text.toString()
        // params["batch_name"] = binding.batchNameEt.text.toString()
        params["password"] = binding.retypeEt.text.toString()
        params["hostal_id"] = hostelId
        params["mess_preference"] = messPref
        params["room_preference"] = roomPref

        lifecycleScope.launch(Dispatchers.IO) {
            val response = ApiService.invoke().privateHostelReg(params)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val root = response.body()
                    if (root?.status == true) {
                        Toast(applicationContext).showCustomToastSucess(
                            root?.message.toString(),
                            this@SignUpMain
                        )
                        startActivity(Intent(this@SignUpMain, MainActivity::class.java))

                    } else {
                        Toast(applicationContext).showCustomToastError(
                            root?.message.toString(),
                            this@SignUpMain
                        )
                    }
                } else {
                    Toast(applicationContext).showCustomToastError(
                        response.message(),
                        this@SignUpMain
                    )
                }
            }

        }


    }


}