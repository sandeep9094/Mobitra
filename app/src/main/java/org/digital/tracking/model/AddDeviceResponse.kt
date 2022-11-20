package org.digital.tracking.model

import com.google.gson.annotations.SerializedName

data class AddDeviceResponse(
    @SerializedName("payload")
    val payload: DeviceListItem,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: String = ""
)


