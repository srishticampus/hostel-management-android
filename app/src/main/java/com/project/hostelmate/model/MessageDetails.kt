package com.project.hostelmate.model

import com.google.gson.annotations.SerializedName

data class MessageDetails(
    @SerializedName("id") var id: String? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("date") var date: String? = null,
    @SerializedName("time") var time: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("userid") var userid: String? = null
)