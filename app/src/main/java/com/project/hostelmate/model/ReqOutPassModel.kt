package com.project.hostelmate.model

import com.google.gson.annotations.SerializedName

data class ReqOutPassModel(
    @SerializedName("status") var status: Boolean? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("outpassDetails") var outpassDetails: ArrayList<OutpassDetails> = arrayListOf()
)