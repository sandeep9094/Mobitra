package org.digital.tracking.maps

import android.animation.ValueAnimator
import android.content.Context
import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import org.digital.tracking.utils.PlayStoreUtil

object MapUtils {

    const val MAP_MARKER_ANCHOR_CENTRE_X_AXIS = 0.5f
    const val MAP_MARKER_ANCHOR_CENTRE_Y_AXIS = 0.5f

    fun getMarkerAnimationDuration(deviceType: String): Long {
        if (deviceType.equals("AARYA141", ignoreCase = true)) {
            return 30 * 1000L
        }
        return 0L
    }

    fun shareLocationLink(context: Context, latitude: Double?, longitude: Double?) {
        val locationLink = "https://maps.google.com/maps?q=loc:" + String.format("%f,%f", latitude, longitude)
        PlayStoreUtil.shareLink(context, locationLink)
    }

    fun moveMarkerSmoothly(marker: Marker?, newLatLng: LatLng, animationDuration: Long): ValueAnimator {
        val animator = ValueAnimator.ofFloat(0f, 100f)
        marker?.let {
            val deltaLatitude = doubleArrayOf(newLatLng.latitude - marker.position.latitude)
            val deltaLongitude = newLatLng.longitude - marker.position.longitude
            val prevStep = floatArrayOf(0f)
            animator.duration = animationDuration
            animator.addUpdateListener { animation ->
                val deltaStep = (animation.animatedValue as Float - prevStep[0]).toDouble()
                prevStep[0] = animation.animatedValue as Float
                val latLng = LatLng(
                    marker.position.latitude + deltaLatitude[0] * deltaStep * 1.0 / 100,
                    marker.position.longitude + deltaStep * deltaLongitude * 1.0 / 100
                )
                marker.position = latLng
            }
        }
        return animator
    }

    fun getLocationBearing(startLocation: LatLng?, destinationLocation: LatLng?): Float {
        if (startLocation == null) {
            return 0f
        }
        if (destinationLocation == null) {
            return 0f
        }

        val location = Location("startLocation")
        location.apply {
            longitude = startLocation.longitude
            latitude = startLocation.latitude
        }

        val bearingToLocation = Location("destinationLocation")
        bearingToLocation.apply {
            longitude = destinationLocation.longitude
            latitude = destinationLocation.latitude
        }
        return location.bearingTo(bearingToLocation)
    }

    fun midPoint(firstLatLng: LatLng, secondLatLng: LatLng): LatLng {
        var lat1 = firstLatLng.latitude
        var lon1 = firstLatLng.longitude
        var lat2 = secondLatLng.latitude
        val dLon = Math.toRadians(secondLatLng.longitude - lon1)
        lat1 = Math.toRadians(lat1)
        lat2 = Math.toRadians(lat2)
        lon1 = Math.toRadians(lon1)
        val Bx = Math.cos(lat2) * Math.cos(dLon)
        val By = Math.cos(lat2) * Math.sin(dLon)
        val lat3 = Math.atan2(Math.sin(lat1) + Math.sin(lat2), Math.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By))
        val lon3 = lon1 + Math.atan2(By, Math.cos(lat1) + Bx)
        return LatLng(lat3, lon3)
    }

    fun defaultMarkerOptions() {
        //TODO move marker options into this wrapper
    }

}