package org.digital.tracking.model


import com.google.gson.annotations.SerializedName

data class DumVehicleAvgRunningResponse(
    @SerializedName("data")
    val data: Data
)


data class VehiclesAverageRunningItem(
    @SerializedName("monthlyKms")
    val monthlyKms: List<Double>?,
    @SerializedName("vehicleNum")
    val vehicleNum: String = "",
    @SerializedName("yearlyKms")
    val yearlyKms: List<Double>?,
    @SerializedName("weeklyKms")
    val weeklyKms: List<Int>?,
    @SerializedName("IMEINumber")
    val iMEINumber: String = ""
)


data class Data(
    @SerializedName("vehiclesAverageRunning")
    val vehiclesAverageRunning: List<VehiclesAverageRunningItem>?
)


