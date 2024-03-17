package com.project.hostelmate

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.project.hostelmate.databinding.ActivitySignUpBinding
import com.project.hostelmate.preference.Constance
import com.project.hostelmate.preference.PrefsHelper
import com.project.hostelmate.preference.showCustomToastError
import com.project.hostelmate.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        PrefsHelper.init(applicationContext)


        binding.btHostelId.setOnClickListener {
            //startActivity(Intent(applicationContext, SignUpMain::class.java))
            verifyHostel()
        }

    }

    fun verifyHostel() {
        val params = HashMap<String?, String?>()
        params["random_id"] = binding.hostelId.text.toString()
        lifecycleScope.launch(Dispatchers.IO) {
            val response = ApiService.invoke().hostelVerification(params)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val root = response.body()
                    if (root?.status == true) {
                        PrefsHelper.write(
                            Constance.HOSTEL_ID,
                            root.hostelId.toString()
                        )
                        PrefsHelper.write(
                            Constance.HOSTEL_TYPE,
                            root.hostelType.toString()
                        )

                        startActivity(Intent(applicationContext, SignUpMain::class.java))


                    } else {
                        Toast(applicationContext).showCustomToastError(
                            root?.message.toString(),
                            this@SignUpActivity
                        )
                    }
                } else {
                    Toast(applicationContext).showCustomToastError(
                        response.message(),
                        this@SignUpActivity
                    )
                }
            }
        }
    }
}