package org.digital.tracking.model.csv

data class LastLocationCsvModel(
    val ImeiNumber: String,
    val VehicleNumber: String,
    val Date: String?,
//    val Address: String,
    val Latitude: Double?,
    val Longitude: Double?,
//    val RawVehicleNumber: String?
)
