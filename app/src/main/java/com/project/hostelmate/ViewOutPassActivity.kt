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
import com.project.hostelmate.databinding.ActivityViewOutPassBinding
import com.project.hostelmate.preference.Constance
import com.project.hostelmate.preference.PrefsHelper
import com.project.hostelmate.preference.showCustomToastError
import com.project.hostelmate.retrofit.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewOutPassActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewOutPassBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityViewOutPassBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        PrefsHelper.init(applicationContext)

        val layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        binding.outPassRv.layoutManager = layoutManager
        viewAllOutPass()

    }

    private fun viewAllOutPass() {
        binding.progressLayout.visibility = View.VISIBLE
        val userId = PrefsHelper.read(Constance.USER_ID)
        val params = HashMap<String?, String?>()
        params["userid"] = userId

        CoroutineScope(Dispatchers.IO).launch {
            val response = ApiService.invoke().viewOutPass(params)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val root = response.body()
                    if (root?.status == true) {
                        val adapter = OutPassAdapter(root.pass, applicationContext)
                        binding.outPassRv.adapter = adapter
                        binding.progressLayout.visibility = View.GONE

                    } else {
                        Toast(applicationContext).showCustomToastError(
                            root?.message.toString(),
                            this@ViewOutPassActivity
                        )
                        binding.progressLayout.visibility = View.GONE

                    }
                } else {
                    Toast(applicationContext).showCustomToastError(
                        response.message(),
                        this@ViewOutPassActivity
                    )
                    binding.progressLayout.visibility = View.GONE

                }
            }
        }

    }

}