package org.digital.tracking.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CreateUser(
    @SerializedName("lastName")
    val lastName: String = "",
    @SerializedName("pincode")
    val pincode: String = "",
    @SerializedName("country")
    val country: String = "",
    @SerializedName("city")
    val city: String = "",
    @SerializedName("credentials")
    val credentials: Credentials,
    @SerializedName("mobile")
    val mobile: String = "",
    @SerializedName("firstName")
    val firstName: String = "",
    @SerializedName("addr_2")
    val address2: String = "",
    @SerializedName("addr_3")
    val address3: String = "",
    @SerializedName("addr_1")
    val address1: String = "",
    @SerializedName("deviceList")
    val deviceList: List<SignUpDeviceItem>?,
    @SerializedName("state")
    val state: String = "",
    @SerializedName("userRole")
    val userRole: String = "",
    @SerializedName("email")
    val email: String = ""
)

@Keep
data class Credentials(
    @SerializedName("username")
    val username: String = "",
    @SerializedName("password")
    val password: String = ""
)


