package com.project.hostelmate.model

import com.google.gson.annotations.SerializedName

data class VisitorpassDetails(
    @SerializedName("visitor_id"   ) var visitorId   : String? = null,
    @SerializedName("userid"       ) var userid      : String? = null,
    @SerializedName("date"         ) var date        : String? = null,
    @SerializedName("time"         ) var time        : String? = null,
    @SerializedName("relation"     ) var relation    : String? = null,
    @SerializedName("visitor_name" ) var visitorName : String? = null
)
