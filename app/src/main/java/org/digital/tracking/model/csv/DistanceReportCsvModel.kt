package org.digital.tracking.model.csv

data class DistanceReportCsvModel(
    val Point: String,
    val VehicleNumber: String,
    val Date: String?,
    val Address: String,
    val Latitude: Double?,
    val Longitude: Double?
)