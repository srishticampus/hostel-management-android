package com.project.hostelmate.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.hostelmate.R
import com.project.hostelmate.model.ChatModel

class ChatAdapter(
    private var chatList: ChatModel,
    private var context: Context,
    private val userId: String
) :
    RecyclerView.Adapter<ChatAdapter.MyViewHolder>() {

    private val holderTypeMessageReceived = 1
    private val holderTypeMessageSent = 2

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val timeTv: TextView = itemView.findViewById(R.id.timeText)
        val msgTv: TextView = itemView.findViewById(R.id.messageText)
        val dateTv: TextView = itemView.findViewById(R.id.dateText)
        val nameTv: TextView = itemView.findViewById(R.id.nameText)

    }

    override fun getItemViewType(position: Int): Int {
        if (chatList.messageDetails.get(position).userid.toString().equals(userId)) {
            return holderTypeMessageSent
        } else {
            return holderTypeMessageReceived
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        when (viewType) {
            holderTypeMessageSent -> {
                val v =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.list_item_message_sent, parent, false)
                return MyViewHolder(v)
            }

            holderTypeMessageReceived -> {
                val v =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.list_item_message_received, parent, false)
                return MyViewHolder(v)
            }

            else -> {
                val v =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.list_item_message_received, parent, false)
                return MyViewHolder(v)
            }
        }

    }

    override fun getItemCount(): Int {
        return chatList.messageDetails.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.timeTv.text = chatList.messageDetails.get(position).time
        holder.msgTv.text = chatList.messageDetails.get(position).message
        holder.dateTv.text = chatList.messageDetails.get(position).date
        holder.nameTv.text = "~ ${chatList.messageDetails.get(position).name}"

    }


}