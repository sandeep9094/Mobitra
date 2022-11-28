package org.digital.tracking.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class DailyReport(
    @SerializedName("startDateTime")
    val startDateTime: String = "",
    @SerializedName("totalDistance")
    val totalDistance: Double = 0.0,
    @SerializedName("endDateTime")
    val endDateTime: String = ""
)

@Keep
data class DailyDistanceReportResponse(
    @SerializedName("a")
    val dailyReport: List<DailyReport>?
)


