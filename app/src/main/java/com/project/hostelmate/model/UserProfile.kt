package com.project.hostelmate.model

import com.google.gson.annotations.SerializedName

data class UserProfile(
    @SerializedName("status"   ) var status   : Boolean?            = null,
    @SerializedName("message"  ) var message  : String?             = null,
    @SerializedName("userData" ) var userData : ArrayList<UserDatax> = arrayListOf()
)
