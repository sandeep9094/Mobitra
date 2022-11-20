package org.digital.tracking.maps

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class Place(
    val vehicleNumber: String,
    val imeiNumber: String,
    val lat: Double,
    val long: Double
) : ClusterItem {
    override fun getPosition(): LatLng = LatLng(lat, long)

    override fun getTitle(): String = vehicleNumber

    override fun getSnippet(): String = imeiNumber
}
