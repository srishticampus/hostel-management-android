package com.project.hostelmate.model

import com.google.gson.annotations.SerializedName

data class ReqVisitorPassModel(
    @SerializedName("status"             ) var status             : Boolean?                      = null,
    @SerializedName("message"            ) var message            : String?                       = null,
    @SerializedName("visitorpassDetails" ) var visitorpassDetails : ArrayList<VisitorpassDetails> = arrayListOf()
)
