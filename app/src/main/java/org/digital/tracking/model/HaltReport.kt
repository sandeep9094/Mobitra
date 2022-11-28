package org.digital.tracking.model

data class HaltReport(
    val imeiNumber: String,
    val vehicleNumber: String?,
    val date: String?,
    val time: String?,
    val latitude: Double?,
    val longitude: Double?,
    val halts: Int?
)
