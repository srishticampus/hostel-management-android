package com.project.hostelmate.model

import com.google.gson.annotations.SerializedName

data class OutPassModel(
    @SerializedName("status"  ) var status  : Boolean?        = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("pass"    ) var pass    : ArrayList<Pass> = arrayListOf()

)
