package org.digital.tracking.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("token")
    val token: String = "",
    @SerializedName("userId")
    val userId: String = ""
)