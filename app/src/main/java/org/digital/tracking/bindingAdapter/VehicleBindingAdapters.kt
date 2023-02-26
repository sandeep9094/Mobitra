package org.digital.tracking.bindingAdapter

import android.os.Build
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.mobitra.tracking.LastLocationsQuery
import org.digital.tracking.BuildConfig
import org.digital.tracking.R
import org.digital.tracking.utils.Constants
import org.digital.tracking.utils.getCompleteAddressString
import org.digital.tracking.utils.setDrawable
import org.digital.tracking.utils.setTintColor

@BindingAdapter(value = ["app:isIgnitionOn", "app:speed"], requireAll = true)
fun ImageView.setIgnitionStatusIconColor(isIgnitionOn: Boolean, speed: Double?) {
    val currentSpeed: Int = speed?.toInt() ?: 0
    if (currentSpeed <= 0 && !isIgnitionOn) {
        this.setTintColor(R.color.vehicle_status_icon_red)
        return
    }
    this.setTintColor(R.color.vehicle_status_icon_green)
}

@BindingAdapter("app:setVehicleStatus")
fun ImageView.setVehicleStatus(status: String) {
    if (status.contains(Constants.VEHICLE_STATUS_KEY_IDLE, ignoreCase = true)) {
        this.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_circle_24_yellow))
    } else if (status.contains(Constants.VEHICLE_STATUS_KEY_RUNNING, ignoreCase = true)) {
        this.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_circle_24_green))
    } else if (status.contains(Constants.VEHICLE_STATUS_KEY_STOP, ignoreCase = true)) {
        this.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_circle_12_red))
    } else {
        this.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_circle_24_grey))
    }
}

@BindingAdapter("app:setVehicleNumberColor")
fun TextView.setVehicleNumberColor(status: String) {
    if (status.contains(Constants.VEHICLE_STATUS_KEY_IDLE, ignoreCase = true)) {
        this.setTextColor(ContextCompat.getColor(context, R.color.bg_yellow))
    } else if (status.contains(Constants.VEHICLE_STATUS_KEY_RUNNING, ignoreCase = true)) {
        this.setTextColor(ContextCompat.getColor(context, R.color.bg_green))
    } else if (status.contains(Constants.VEHICLE_STATUS_KEY_STOP, ignoreCase = true)) {
        this.setTextColor(ContextCompat.getColor(context, R.color.bg_red))
    } else {
        this.setTextColor(ContextCompat.getColor(context, R.color.bg_grey))
    }
}

@BindingAdapter("app:setVehicleStatusIconColor")
fun ImageView.setVehicleStatusIconColor(status: String) {
    if (status.contains(Constants.VEHICLE_STATUS_KEY_IDLE, ignoreCase = true)) {
        this.setTintColor(R.color.bg_yellow)
    } else if (status.contains(Constants.VEHICLE_STATUS_KEY_RUNNING, ignoreCase = true)) {
        this.setTintColor(R.color.bg_green)
    } else if (status.contains(Constants.VEHICLE_STATUS_KEY_STOP, ignoreCase = true)) {
        this.setTintColor(R.color.bg_red)
    } else {
        this.setTintColor(R.color.bg_grey)
    }
}

fun ImageView.setVehicleSignalColor(gsmStr: Double?) {
    val gsmInt: Int = gsmStr?.toInt() ?: 0
    if (gsmInt <= Constants.GSM_SIGNAL_STRENGTH_WEEK_UPPER_LIMIT) {
        this.setTintColor(R.color.icon_tint_red)
    } else if (gsmInt <= Constants.GSM_SIGNAL_STRENGTH_NORMAL_UPPER_LIMIT) {
        this.setTintColor(R.color.icon_tint_yellow)
    } else if (gsmInt >= Constants.GSM_SIGNAL_STRENGTH_STRONG_LOWER_LIMIT) {
        this.setTintColor(R.color.icon_tint_green)
    }
}

@BindingAdapter("app:setGpsIconColor")
fun ImageView.setGpsIconColor(value: Int?) {
    if (value == null) {
        this.setTintColor(R.color.icon_tint_red)
        return
    }
    if (value == Constants.GPS_FIXED_STATE_ON) {
        this.setTintColor(R.color.icon_tint_green)
    } else {
        this.setTintColor(R.color.icon_tint_red)
    }
}


@BindingAdapter("app:setVehicleStatusText")
fun TextView.setVehicleStatusText(status: String) {
    if (status.contains(Constants.VEHICLE_STATUS_KEY_IDLE, ignoreCase = true)) {
        this.text = context.getString(R.string.idle)
    } else if (status.contains(Constants.VEHICLE_STATUS_KEY_RUNNING, ignoreCase = true)) {
        this.text = context.getString(R.string.running)
    } else if (status.contains(Constants.VEHICLE_STATUS_KEY_STOP, ignoreCase = true)) {
        this.text = context.getString(R.string.stop)
    } else {
        this.text = context.getString(R.string.unknown)
    }
}

@BindingAdapter("app:setVehicleIconStatus")
fun ImageView.setVehicleIconStatus(status: String) {
    if (status.contains(Constants.VEHICLE_STATUS_KEY_IDLE, ignoreCase = true)) {
        this.setDrawable(context, R.drawable.car_ideal)
    } else if (status.contains(Constants.VEHICLE_STATUS_KEY_RUNNING, ignoreCase = true)) {
        this.setDrawable(context, R.drawable.car_running)
    } else if (status.contains(Constants.VEHICLE_STATUS_KEY_STOP, ignoreCase = true)) {
        this.setDrawable(context, R.drawable.car_stop)
    } else {
        this.setDrawable(context, R.drawable.car_ideal)
    }
}

@BindingAdapter(value = ["app:setAddressLat", "app:setAddressLong"], requireAll = true)
fun TextView.setCompleteAddress(latitude: Double?, longitude: Double?) {
    val address = this.context.getCompleteAddressString(latitude, longitude)
    this.text = address
}

fun LastLocationsQuery.LastLocation?.getVehicleStatus(): String {
    val speedInt: Int = this?.speed?.toInt() ?: 0
    val ignitionInt: Int = this?.ignitionStat?.toInt() ?: Constants.IGNITION_STAT_OFF
    var vehicleStatus: String = Constants.VEHICLE_STATUS_KEY_UNKNOWN
    if (ignitionInt == Constants.IGNITION_STAT_ON && speedInt <= 5) {
        vehicleStatus = Constants.VEHICLE_STATUS_KEY_IDLE
    } else if (ignitionInt == Constants.IGNITION_STAT_OFF) {
        vehicleStatus = Constants.VEHICLE_STATUS_KEY_STOP
    } else if (ignitionInt == Constants.IGNITION_STAT_ON && speedInt > 5) {
        vehicleStatus = Constants.VEHICLE_STATUS_KEY_RUNNING
    }
    return vehicleStatus
}

