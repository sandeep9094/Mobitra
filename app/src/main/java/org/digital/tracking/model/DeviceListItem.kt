package org.digital.tracking.model

import com.google.gson.annotations.SerializedName

data class DeviceListItem(
    @SerializedName("deviceType")
    val deviceType: String = "",
    @SerializedName("imei")
    val imei: String = "",
    @SerializedName("device")
    val name: String = "",
    @SerializedName("simNumber")
    val simNumber: String = "",
    @SerializedName("vehicleType")
    val vehicleType: String = "",
    @SerializedName("referenceId")
    val referenceId: String = "",
    @SerializedName("deviceId")
    val deviceId: String = ""
)
