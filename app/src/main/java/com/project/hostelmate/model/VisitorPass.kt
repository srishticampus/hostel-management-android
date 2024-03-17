package com.project.hostelmate.model

import com.google.gson.annotations.SerializedName

data class VisitorPass(
    @SerializedName("id"             ) var id            : String? = null,
    @SerializedName("visitor_name"   ) var visitorName   : String? = null,
    @SerializedName("userid"         ) var userid        : String? = null,
    @SerializedName("relation"       ) var relation      : String? = null,
    @SerializedName("pass_date"      ) var passDate      : String? = null,
    @SerializedName("pass_time"      ) var passTime      : String? = null,
    @SerializedName("visitor_status" ) var visitorStatus : String? = null,
    @SerializedName("pass_file"      ) var passFile      : String? = null
)
