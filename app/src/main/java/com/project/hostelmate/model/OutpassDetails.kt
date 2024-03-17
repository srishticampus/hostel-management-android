package com.project.hostelmate.model

import com.google.gson.annotations.SerializedName

data class OutpassDetails(
    @SerializedName("outpass_id"  ) var outpassId  : String? = null,
    @SerializedName("userid"      ) var userid     : String? = null,
    @SerializedName("date"        ) var date       : String? = null,
    @SerializedName("time"        ) var time       : String? = null,
    @SerializedName("purpose"     ) var purpose    : String? = null,
    @SerializedName("return_time" ) var returnTime : String? = null
)
