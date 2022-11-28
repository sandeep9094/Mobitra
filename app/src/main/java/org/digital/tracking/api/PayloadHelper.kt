package org.digital.tracking.api

import com.google.gson.JsonObject
import org.digital.tracking.model.CreateUser
import org.digital.tracking.utils.Constants

object PayloadHelper {

    fun loginPayload(emailOrPhone: String, password: String): JsonObject {
        val payload = JsonObject()
        payload.addProperty("username", emailOrPhone)
        payload.addProperty("password", password)

        return payload
    }

    fun signUpPayload(user: CreateUser) {

    }

    fun addNewDevicePayload(imei: String, simNumber: String, vehicleType: String, vehicleNumber: String, device: String): JsonObject {
        val payload = JsonObject()
        payload.addProperty("imei", imei)
        payload.addProperty("simNumber", simNumber)
        payload.addProperty("vehicleType", vehicleType)
        payload.addProperty("vehicleNumber", vehicleNumber)
        payload.addProperty("device", device)
        payload.addProperty("deviceType", "Electronic")
        payload.addProperty("referenceId", Constants.DEVICE_REFERENCE_ID)

        return payload
    }

}