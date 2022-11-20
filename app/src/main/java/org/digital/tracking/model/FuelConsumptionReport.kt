package org.digital.tracking.model

data class FuelConsumptionReport(
    val vehicleNumber: String,
    val totalKm: String,
    val fuelConsumptionInLtr: String
)
