package org.digital.tracking.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import org.digital.tracking.BuildConfig

val DAILY_REPORT_SERIALIZED_NAME: String = if (BuildConfig.DEBUG) {
    "dailyReport"
} else {
    "a"
}


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
//    @SerializedName("dailyReport") //Debug
    @SerializedName("a") //Release
    val dailyReport: List<DailyReport>?
)




