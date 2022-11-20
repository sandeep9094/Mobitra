package org.digital.tracking.model

import com.google.gson.annotations.SerializedName
import org.digital.tracking.utils.Constants

data class SignUpDeviceItem(
    @SerializedName("deviceType")
    val deviceType: String = "",
    @SerializedName("imei")
    val imei: String = "",
    @SerializedName("device")
    val device: String = "",
    @SerializedName("simNumber")
    val simNumber: String = "",
    @SerializedName("vehicleType")
    val vehicleType: String = "",
    @SerializedName("referenceId")
    val referenceId: String = Constants.DEVICE_REFERENCE_ID
)