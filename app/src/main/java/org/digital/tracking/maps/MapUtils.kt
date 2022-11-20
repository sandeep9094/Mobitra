package org.digital.tracking.maps

import android.app.Activity
import android.content.Context
import org.digital.tracking.utils.PlayStoreUtil

object MapUtils {

    fun shareLocationLink(context: Context, latitude: Double?, longitude: Double?) {
        val locationLink = "https://maps.google.com/maps?q=loc:" + String.format("%f,%f", latitude, longitude)
        PlayStoreUtil.shareLink(context, locationLink)
    }

}