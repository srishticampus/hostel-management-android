package com.project.hostelmate.model

import com.google.gson.annotations.SerializedName

data class ChangeMessPref(
    @SerializedName("status") var status: Boolean? = null,
    @SerializedName("message") var message: String? = null
)
