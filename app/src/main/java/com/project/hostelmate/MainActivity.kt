package com.project.hostelmate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.project.hostelmate.databinding.ActivityMainBinding
import com.project.hostelmate.preference.Constance
import com.project.hostelmate.preference.PrefsHelper
import com.project.hostelmate.preference.showCustomToastError
import com.project.hostelmate.retrofit.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var phoneNumber: String
    lateinit var password: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
       // Thread.sleep(2000)
       // installSplashScreen()
       // enableEdgeToEdge()
        setContentView(view)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        PrefsHelper.init(applicationContext)



        binding.signUpBt.setOnClickListener {
            startActivity(Intent(applicationContext, SignUpActivity::class.java))
        }

        binding.loginBt.setOnClickListener {
            validateData()
        }


    }

    private fun loginApi() {
        binding.progressLayout.visibility = View.VISIBLE
        val params = HashMap<String?, String?>()
        params["phone"] = phoneNumber
        params["device_token"] = getDeviceId(applicationContext)
        params["password"] = password

        CoroutineScope(Dispatchers.IO).launch {
            val response = ApiService.invoke().loginuser(params)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val root = response.body()
                    if (root?.status == true) {
                        PrefsHelper.writeBool(Constance.IS_LOGGED_IN, true)
                        PrefsHelper.write(Constance.USER_ID, root.userData.get(0).id.toString())
                        PrefsHelper.write(
                            Constance.USER_NAME,
                            root.userData.get(0).name.toString()
                        )
                        PrefsHelper.write(
                            Constance.BATCH_NAME,
                            root.userData.get(0).batchName.toString()
                        )
                        PrefsHelper.write(
                            Constance.MESS_PREFERENCE,
                            root.userData.get(0).messPreference.toString()
                        )
                        PrefsHelper.write(
                            Constance.ROOM_PREFERENCE,
                            root.userData.get(0).roomPreference.toString()
                        )
                        PrefsHelper.write(
                            Constance.HOSTEL_TYPE,
                            root.userData.get(0).hostalType.toString()
                        )
                        PrefsHelper.write(
                            Constance.HOSTEL_UNIQUE_ID,
                            root.userData.get(0).uniqueIdIs.toString()
                        )
                        PrefsHelper.write(
                            Constance.HOSTEL_ID,
                            root.userData.get(0).hostalId.toString()
                        )
                        binding.progressLayout.visibility = View.GONE
                        binding.loginLayout.visibility = View.GONE
                        startActivity(Intent(applicationContext, HomeActivity::class.java))
                        startActivity(intent)
                        finish()
                    } else {
                        binding.progressLayout.visibility = View.GONE

                        Toast(applicationContext).showCustomToastError(
                            root?.message.toString(),
                            this@MainActivity
                        )
                    }

                } else {
                    binding.progressLayout.visibility = View.GONE
                    Toast(applicationContext).showCustomToastError(
                        response.message(),
                        this@MainActivity
                    )
                }

            }
        }

    }

    private fun validateData() {
        val inputString = binding.phoneEt.text.toString()
        phoneNumber = inputString.replace("[\\s-()]".toRegex(), "")
        if (phoneNumber.length >= 10) {
            if (binding.passwordEt.text.isEmpty()) {
                Toast(this).showCustomToastError("Enter your Password", this)

            } else {
                password = binding.passwordEt.text.toString()
                try {
                    loginApi()

                } catch (e: Exception) {

                }
            }
        } else {
            Toast(this).showCustomToastError("Enter a valid phone number", this)

        }
    }

    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }



}