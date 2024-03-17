package com.project.hostelmate.model

import com.google.gson.annotations.SerializedName

data class MessDetails(
    @SerializedName("id"              ) var id             : String? = null,
    @SerializedName("mess_preference" ) var messPreference : String? = null
)
