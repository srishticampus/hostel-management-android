package com.project.hostelmate

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.hostelmate.adapter.OutPassAdapter
import com.project.hostelmate.adapter.VisitorPassAdapter
import com.project.hostelmate.databinding.ActivityViewVisitorPassBinding
import com.project.hostelmate.preference.Constance
import com.project.hostelmate.preference.PrefsHelper
import com.project.hostelmate.preference.showCustomToastError
import com.project.hostelmate.retrofit.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewVisitorPassActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewVisitorPassBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityViewVisitorPassBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        PrefsHelper.init(applicationContext)

        val layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        binding.visitorPassRv.layoutManager = layoutManager
        viewAllVisitorPass()


    }

    private fun viewAllVisitorPass() {
        binding.progressLayout.visibility = View.VISIBLE
        val userId = PrefsHelper.read(Constance.USER_ID)
        val params = HashMap<String?, String?>()
        params["userid"] = userId

        CoroutineScope(Dispatchers.IO).launch {
            val response = ApiService.invoke().viewVisitorPass(params)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val root = response.body()
                    if (root?.status == true) {
                        val adapter = VisitorPassAdapter(root.pass, applicationContext)
                        binding.visitorPassRv.adapter = adapter
                        binding.progressLayout.visibility = View.GONE

                    } else {
                        Toast(applicationContext).showCustomToastError(
                            root?.message.toString(),
                            this@ViewVisitorPassActivity
                        )
                        binding.progressLayout.visibility = View.GONE

                    }
                } else {
                    Toast(applicationContext).showCustomToastError(
                        response.message(),
                        this@ViewVisitorPassActivity
                    )
                    binding.progressLayout.visibility = View.GONE

                }
            }
        }
    }
}