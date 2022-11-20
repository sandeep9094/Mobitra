package org.digital.tracking.model


import com.google.gson.annotations.SerializedName

data class PasswordGenerateOtpResponse(
    @SerializedName("payload")
    val payload: OtpPayload,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: String = ""
)


data class OtpPayload(
    @SerializedName("otp_sent")
    val otpSent: Boolean = false
)


