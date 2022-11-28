package org.digital.tracking.maps

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import org.digital.tracking.utils.PlayStoreUtil

object MapUtils {

    fun shareLocationLink(context: Context, latitude: Double?, longitude: Double?) {
        val locationLink = "https://maps.google.com/maps?q=loc:" + String.format("%f,%f", latitude, longitude)
        PlayStoreUtil.shareLink(context, locationLink)
    }

    fun moveMarkerSmoothly(marker: Marker?, newLatLng: LatLng): ValueAnimator {
        val animator = ValueAnimator.ofFloat(0f, 100f)
        marker?.let {
            val deltaLatitude = doubleArrayOf(newLatLng.latitude - marker.position.latitude)
            val deltaLongitude = newLatLng.longitude - marker.position.longitude
            val prevStep = floatArrayOf(0f)
            animator.duration = 1 * 1000
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

}