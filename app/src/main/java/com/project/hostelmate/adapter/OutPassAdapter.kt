package com.project.hostelmate.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.hostelmate.R
import com.project.hostelmate.model.OutPassModel
import com.project.hostelmate.model.Pass

class OutPassAdapter(
    private var outPassList: ArrayList<Pass>?,
    private var context: Context
) : RecyclerView.Adapter<OutPassAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val outPassStatus: TextView = itemView.findViewById(R.id.view_out_pass_status)
        val fileUploadTrueIv: ImageView = itemView.findViewById(R.id.file_upload_true)
        val fileUploadFalseIv: ImageView = itemView.findViewById(R.id.file_upload_false)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.rv_out_pass_layout, parent, false)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return outPassList?.size ?: 0
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        when (outPassList?.get(position)?.outpassStatus.toString()) {
            "0" -> {
                holder.outPassStatus.text = "Pending"
                holder.fileUploadFalseIv.visibility = View.VISIBLE
                holder.fileUploadTrueIv.visibility = View.GONE
            }

            "1" -> {
                holder.outPassStatus.text = "Approved"
                holder.fileUploadFalseIv.visibility = View.GONE
                holder.fileUploadTrueIv.visibility = View.VISIBLE
            }

            "2" -> {
                holder.outPassStatus.text = "Pending"
                holder.fileUploadFalseIv.visibility = View.VISIBLE
                holder.fileUploadTrueIv.visibility = View.GONE
            }

            else -> {

            }
        }

        holder.fileUploadTrueIv.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(outPassList?.get(position)?.passFile)
            )
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }


    }


}