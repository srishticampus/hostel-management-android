package com.project.hostelmate.model

import com.google.gson.annotations.SerializedName

data class VisitorPassModel(
    @SerializedName("status"  ) var status  : Boolean?        = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("pass"    ) var pass    : ArrayList<VisitorPass> = arrayListOf()
)
