package com.project.hostelmate.model

import com.google.gson.annotations.SerializedName

data class ChatModel(
    @SerializedName("status"         ) var status         : Boolean?                  = null,
    @SerializedName("message"        ) var message        : String?                   = null,
    @SerializedName("messageDetails" ) var messageDetails : ArrayList<MessageDetails> = arrayListOf()
)
