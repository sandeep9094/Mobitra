package org.digital.tracking.model


import com.google.gson.annotations.SerializedName

data class VerifyOtpResponse(
    @SerializedName("payload")
    val payload: VerifyOtpPayload,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: String = ""
)


data class VerifyOtpPayload(
    @SerializedName("otp_validated")
    val otpValidated: Boolean = false
)


