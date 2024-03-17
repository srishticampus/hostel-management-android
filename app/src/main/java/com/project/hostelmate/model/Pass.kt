package com.project.hostelmate.model

import com.google.gson.annotations.SerializedName

data class Pass(

    @SerializedName("id") var id: String? = null,
    @SerializedName("userid") var userid: String? = null,
    @SerializedName("purpose") var purpose: String? = null,
    @SerializedName("pass_date") var passDate: String? = null,
    @SerializedName("pass_time") var passTime: String? = null,
    @SerializedName("return_time") var returnTime: String? = null,
    @SerializedName("outpass_status") var outpassStatus: String? = null,
    @SerializedName("pass_file") var passFile: String? = null
)
