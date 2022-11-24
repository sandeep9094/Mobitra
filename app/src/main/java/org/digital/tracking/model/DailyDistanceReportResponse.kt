package org.digital.tracking.model


import com.google.gson.annotations.SerializedName

data class DailyReport(
    @SerializedName("startDateTime")
    val startDateTime: String = "",
    @SerializedName("totalDistance")
    val totalDistance: Double = 0.0,
    @SerializedName("endDateTime")
    val endDateTime: String = ""
)


data class DailyDistanceReportResponse(
    @SerializedName("dailyReport")
    val dailyReport: List<DailyReport>?
)


