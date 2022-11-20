package org.digital.tracking.model


import com.google.gson.annotations.SerializedName

data class GetUserResponse(
    @SerializedName("payload")
    val payload: User,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: String = ""
)
