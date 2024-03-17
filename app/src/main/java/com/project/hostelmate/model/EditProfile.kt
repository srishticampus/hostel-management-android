package com.project.hostelmate.model

import com.google.gson.annotations.SerializedName

data class EditProfile(
    @SerializedName("status"  ) var status  : Boolean? = null,
    @SerializedName("message" ) var message : String?  = null
)
