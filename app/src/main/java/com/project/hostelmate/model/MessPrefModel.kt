package com.project.hostelmate.model

import com.google.gson.annotations.SerializedName

data class MessPrefModel(
    @SerializedName("status"  ) var status  : Boolean? = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("details" ) var details : MessDetails? = MessDetails()
)
