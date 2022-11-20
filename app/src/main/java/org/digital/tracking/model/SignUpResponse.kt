package org.digital.tracking.model


import com.google.gson.annotations.SerializedName

data class SignUpResponse(
    @SerializedName("payload")
    val payload: User,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: String = ""
)


data class User(
    @SerializedName("lastName")
    val lastName: String = "",
    @SerializedName("pincode")
    val pincode: String = "",
    @SerializedName("country")
    val country: String = "",
    @SerializedName("city")
    val city: String = "",
    @SerializedName("mobile")
    val mobile: String = "",
    @SerializedName("userId")
    val userId: String = "",
    @SerializedName("firstName")
    val firstName: String = "",
    @SerializedName("addr_2")
    val addr2: String = "",
    @SerializedName("addr_3")
    val addr3: String = "",
    @SerializedName("addr_1")
    val addr1: String = "",
    @SerializedName("deviceList")
    val deviceList: List<DeviceListItem>?,
    @SerializedName("state")
    val state: String = "",
    @SerializedName("userRole")
    val userRole: String = "",
    @SerializedName("email")
    val email: String = "",
    @SerializedName("imei")
    val imei: String = ""
)


