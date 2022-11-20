package org.digital.tracking.model

import android.os.Parcelable
import androidx.core.view.ContentInfoCompat
import kotlinx.parcelize.Parcelize

@Parcelize
data class VehicleModel(
    val vehicleNumber: String,
    val status: String,
    val speed: String,
    val todayDistance: String,
    val address: String,
    val type: String,
    val isVehicleParking: Boolean = false,
    val lat: Double,
    val long: Double,
    val lastContact: String,
    val ignition: Boolean
) : Parcelable
