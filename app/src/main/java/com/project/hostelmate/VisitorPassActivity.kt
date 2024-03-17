package com.project.hostelmate

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.project.hostelmate.databinding.ActivityVisitorPassBinding
import com.project.hostelmate.preference.Constance
import com.project.hostelmate.preference.PrefsHelper
import com.project.hostelmate.preference.showCustomToastError
import com.project.hostelmate.preference.showCustomToastSucess
import com.project.hostelmate.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class VisitorPassActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVisitorPassBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityVisitorPassBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        PrefsHelper.init(applicationContext)


        binding.dateTv.setOnClickListener {
            showDatePicker(binding.dateTv)
        }
        binding.timeTv.setOnClickListener {
            showTimePicker(binding.timeTv)
        }
        binding.visitorPassBt.setOnClickListener {
            validateData()
        }


    }

    private fun showDatePicker(tv: TextView) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        // Create DatePickerDialog and set OnDateSetListener
        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                // Set selected date on TextView
                val selectedDate = formatDate(year, month, dayOfMonth)
                //  val selectedDate = "$year-${month + 1}-$dayOfMonth"
                tv.text = selectedDate
            }, year, month, dayOfMonth
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        // Show DatePickerDialog
        datePickerDialog.show()
    }

    private fun formatDate(year: Int, month: Int, dayOfMonth: Int): String {
        // Create a SimpleDateFormat object with the desired format
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        // Create a Calendar object and set the selected date
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        // Format the date and return the formatted string
        return sdf.format(calendar.time)
    }

    private fun showTimePicker(tv: TextView) {
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        // Create TimePickerDialog and set OnTimeSetListener
        val timePickerDialog = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                // Format the selected time
                val selectedTime = formatTime(hourOfDay, minute)

                // val selectedTime =
                String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute)
                // Set selected time on TextView
                tv.text = selectedTime
            },
            hourOfDay,
            minute,
            true // set to true if using 24-hour format, false for 12-hour format
        )

        // Show TimePickerDialog
        timePickerDialog.show()
    }

    private fun formatTime(hour: Int, minute: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        val sdf = android.text.format.DateFormat.getTimeFormat(applicationContext)
        return sdf.format(calendar.time)
    }

    private fun validateData() {
        if (binding.visitorNameEt.text.isEmpty() || binding.visitorRelationEt.text.isEmpty() ||
            binding.dateTv.text.isEmpty() || binding.timeTv.text.isEmpty()
        ) {
            Toast(this).showCustomToastError("fill the details", this)
        } else {
            reqVisitorPass()
        }
    }

    private fun reqVisitorPass() {

        binding.progressLayout.visibility = View.VISIBLE
        val userId = PrefsHelper.read(Constance.USER_ID)
        val date = binding.dateTv.text.toString()
        val time = binding.timeTv.text.toString()
        val relation = binding.visitorRelationEt.text.toString()
        val visitorName = binding.visitorNameEt.text.toString()

        val params = HashMap<String?, String?>()
        params["user_id"] = userId
        params["date"] = date
        params["time"] = time
        params["relation"] = relation
        params["visitor_name"] = visitorName
        lifecycleScope.launch(Dispatchers.IO) {
            val response = ApiService.invoke().reqVisitorPass(params)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val root = response.body()
                    if (root?.status == true) {
                        binding.progressLayout.visibility = View.GONE
                        Toast(applicationContext).showCustomToastSucess(
                            root.message.toString(), this@VisitorPassActivity
                        )
                        startActivity(Intent(this@VisitorPassActivity, HomeActivity::class.java))
                    } else {
                        Toast(applicationContext).showCustomToastError(
                            root?.message.toString(),
                            this@VisitorPassActivity
                        )
                        binding.progressLayout.visibility = View.GONE
                    }
                } else {
                    Toast(applicationContext).showCustomToastError(
                        response.message(),
                        this@VisitorPassActivity
                    )
                    binding.progressLayout.visibility = View.GONE
                }
            }
        }

    }
}