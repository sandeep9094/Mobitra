package org.digital.tracking.model

import com.google.gson.annotations.SerializedName

data class ChangePasswordPayload(
    @SerializedName("password_changed")
    val passwordChanged: Boolean = false
)


data class ChangePasswordResponse(
    @SerializedName("payload")
    val payload: ChangePasswordPayload,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: String = ""
)


