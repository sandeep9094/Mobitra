package org.digital.tracking.model.csv

data class HaltReportCsvModel(
    val ImeiNumber: String,
    val VehicleNumber: String,
    val StartDate: String?,
    val EndDate: String?,
    val Address: String,
    val Latitude: Double?,
    val Longitude: Double?,
    val TotalHalts: Int?
)
