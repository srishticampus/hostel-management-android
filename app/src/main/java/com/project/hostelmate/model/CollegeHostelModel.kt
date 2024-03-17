package com.project.hostelmate.model

import com.google.gson.annotations.SerializedName

data class CollegeHostelModel(
    @SerializedName("id"              ) var id             : String? = null,
    @SerializedName("name"            ) var name           : String? = null,
    @SerializedName("contact_number"  ) var contactNumber  : String? = null,
    @SerializedName("email"           ) var email          : String? = null,
    @SerializedName("address"         ) var address        : String? = null,
    @SerializedName("batch_name"      ) var batchName      : String? = null,
    @SerializedName("mess_preference" ) var messPreference : String? = null,
    @SerializedName("room_preference" ) var roomPreference : String? = null,
    @SerializedName("hostal_type"     ) var hostalType     : String? = null,
    @SerializedName("password"        ) var password       : String? = null,
    @SerializedName("unique_id_is"    ) var uniqueIdIs     : String? = null
)