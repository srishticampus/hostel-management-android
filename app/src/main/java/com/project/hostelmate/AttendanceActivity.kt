package com.project.hostelmate

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.project.hostelmate.databinding.ActivityAttendanceBinding
import com.project.hostelmate.preference.Constance
import com.project.hostelmate.preference.PrefsHelper
import com.project.hostelmate.preference.showCustomToastError
import com.project.hostelmate.preference.showCustomToastSucess
import com.project.hostelmate.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AttendanceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAttendanceBinding
    private lateinit var codeScanner: CodeScanner


    companion object {
        private const val CAMERA_PERMISSION_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendanceBinding.inflate(layoutInflater)
        val view = binding.root
        enableEdgeToEdge()
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        PrefsHelper.init(applicationContext)



        checkPermission(
            Manifest.permission.CAMERA,
            CAMERA_PERMISSION_CODE
        )

    }

    fun scanFun() {
        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)
        codeScanner = CodeScanner(this, scannerView)

        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                //Toast.makeText(this, "Scan result: ${it.text}", Toast.LENGTH_LONG).show()
                PrefsHelper.write(Constance.ATTENDANCE_CODE, it.text.toString())

                markAttendance()


            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(
                    this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    // Function to check and request permission.
    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                this@AttendanceActivity,
                permission
            ) == PackageManager.PERMISSION_DENIED
        ) {

            // Requesting the permission
            ActivityCompat.requestPermissions(
                this@AttendanceActivity,
                arrayOf(permission),
                requestCode
            )
        } else {

            //TODO scanning
            scanFun()

            Toast.makeText(
                this@AttendanceActivity,
                "Permission already granted",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // This function is called when the user accepts or decline the permission.
    // Request Code is used to check which permission called this function.
    // This request code is provided when the user is prompt for permission.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //TODO scanning
                scanFun()

                Toast.makeText(
                    this@AttendanceActivity,
                    "Camera Permission Granted",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this@AttendanceActivity,
                    "Camera Permission Denied",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun markAttendance() {
        val uniqueId = PrefsHelper.read(Constance.ATTENDANCE_CODE)
        val userId = PrefsHelper.read(Constance.USER_ID)

        val params = HashMap<String?, String?>()
        params["user_id"] = userId
        params["unique_id"] = uniqueId

        lifecycleScope.launch(Dispatchers.IO) {
            val response = ApiService.invoke().markAttendance(params)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val root = response.body()
                    if (root?.status == true) {
                        Toast(applicationContext).showCustomToastSucess(
                            root?.message.toString(),
                            this@AttendanceActivity
                        )
                        startActivity(Intent(applicationContext, HomeActivity::class.java))
                    } else {
                        Toast(applicationContext).showCustomToastError(
                            root?.message.toString(),
                            this@AttendanceActivity
                        )
                        startActivity(Intent(applicationContext, HomeActivity::class.java))

                    }
                } else {
                    Toast(applicationContext).showCustomToastError(
                        response.message(),
                        this@AttendanceActivity
                    )
                    startActivity(Intent(applicationContext, HomeActivity::class.java))
                }
            }
        }


    }
}

