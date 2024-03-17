package com.project.hostelmate.model

import com.google.gson.annotations.SerializedName

data class VerifyHostel(
    @SerializedName("status"     ) var status     : Boolean? = null,
    @SerializedName("message"    ) var message    : String?  = null,
    @SerializedName("hostelType" ) var hostelType : String?  = null,
    @SerializedName("hostelId"   ) var hostelId   : String?  = null
)
