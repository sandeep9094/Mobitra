package org.digital.tracking.model

data class LastLocationReport(
    val imeiNumber: String,
    val vehicleNumber: String,
    val date: String?,
    val time: String?,
    val address: String,
    val lat: Double?,
    val long: Double?
)
