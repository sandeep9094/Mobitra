package org.digital.tracking.model

import com.mobitra.tracking.ReportsQuery

data class CustomDistanceReport(
    val imeiNumber: String?,
    val vehicleNumber: String?,
    val isStartPoint: Boolean = true,
    val startPoint: ReportsQuery.StartPoint?,
    val endPoint: ReportsQuery.EndPoint?
)