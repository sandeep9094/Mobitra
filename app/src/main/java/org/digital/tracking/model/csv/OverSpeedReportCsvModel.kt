package org.digital.tracking.model.csv

data class OverSpeedReportCsvModel(
    val ImeiNumber: String,
    val VehicleNumber: String,
    val Date: String?,
    val Latitude: Double?,
    val Longitude: Double?,
    val Speed: String
)