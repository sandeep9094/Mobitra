package org.digital.tracking.model

data class HaltReport(
    val imeiNumber: String,
    val vehicleNumber: String?,
    val fromDate: String?,
    val toDate: String?,
    val latitude: Double?,
    val longitude: Double?,
    val halts: Int?
)
