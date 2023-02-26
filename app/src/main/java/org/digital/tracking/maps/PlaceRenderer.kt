package org.digital.tracking.maps

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import org.digital.tracking.R
import org.digital.tracking.bindingAdapter.getVehicleStatus
import org.digital.tracking.repository.VehicleRepository
import org.digital.tracking.utils.BitmapHelper
import org.digital.tracking.utils.Constants
import org.digital.tracking.view.activity.LiveLocationActivity
import timber.log.Timber

/**
 * A custom cluster renderer for Place objects.
 */
class PlaceRenderer(
    private val context: FragmentActivity,
    private val map: GoogleMap,
    clusterManager: ClusterManager<Place>
) : DefaultClusterRenderer<Place>(context, map, clusterManager) {

    /**
     * The icon to use for each cluster item
     */
    private val bicycleIcon: BitmapDescriptor by lazy {
        val color = ContextCompat.getColor(
            context,
            R.color.colorPrimary
        )
        BitmapHelper.vectorToBitmap(
            context,
            R.drawable.car_red,
//            color
        )
    }

    /**
     * Method called before the cluster item (i.e. the marker) is rendered. This is where marker
     * options should be set
     */
    override fun onBeforeClusterItemRendered(item: Place, markerOptions: MarkerOptions) {
        val imeiNumber = item.imeiNumber
        val vehicleLat: Double = item.lat
        val vehicleLong: Double = item.long
        markerOptions.title(imeiNumber)
            .position(LatLng(vehicleLat, vehicleLong))
            .icon(bicycleIcon)
    }


    /**
     * Method called right after the cluster item (i.e. the marker) is rendered. This is where
     * properties for the Marker object should be set.
     */
    override fun onClusterItemRendered(clusterItem: Place, marker: Marker) {
        marker.tag = "${clusterItem.imeiNumber},${clusterItem.vehicleNumber}"
        map.setOnMarkerClickListener {
            val markerTag = it.tag.toString().split(",")
            if (markerTag.isEmpty()) {
                return@setOnMarkerClickListener false
            }
            if (markerTag.size <= 1) {
                return@setOnMarkerClickListener false
            }
            val imeiNumber: String = markerTag[0]
            val vehicleNumber: String = markerTag[1]
            openLiveTracking(imeiNumber, vehicleNumber, it.position.latitude, it.position.longitude)
            return@setOnMarkerClickListener true
        }
    }

    override fun setOnClusterInfoWindowClickListener(listener: ClusterManager.OnClusterInfoWindowClickListener<Place>?) {
        super.setOnClusterInfoWindowClickListener(listener)
        Timber.d("setOnClusterInfoWindowClickListener---------------")
    }

    override fun setOnClusterItemInfoWindowClickListener(listener: ClusterManager.OnClusterItemInfoWindowClickListener<Place>?) {
        super.setOnClusterItemInfoWindowClickListener(listener)
        Timber.d("setOnClusterItemInfoWindowClickListener---------------")
    }

    private fun openLiveTracking(vehicleImeiNumber: String, vehicleNumber: String, lat: Double, long: Double) {
        val getLastLocation = VehicleRepository.getVehicleList().find { it?.IMEINumber == vehicleImeiNumber }
        val intent = Intent(context, LiveLocationActivity::class.java)
        val vehicleStatus = getLastLocation.getVehicleStatus()
        intent.putExtra(Constants.INTENT_KEY_VEHICLE_IMEI, getLastLocation?.IMEINumber)
        intent.putExtra(Constants.INTENT_KEY_VEHICLE_NUMBER, vehicleNumber)
        intent.putExtra(Constants.INTENT_KEY_LAST_LAT, lat)
        intent.putExtra(Constants.INTENT_KEY_LAST_LONG, long)
        intent.putExtra(Constants.INTENT_KEY_VEHICLE_SPEED, getLastLocation?.speed)
        intent.putExtra(Constants.INTENT_KEY_VEHICLE_STATUS, vehicleStatus)
        intent.putExtra(Constants.INTENT_KEY_VEHICLE_GPS_STATUS, getLastLocation?.gpsFixState)
        intent.putExtra(Constants.INTENT_KEY_VEHICLE_GSM_STATUS, getLastLocation?.gsmSigStr)
        intent.putExtra(Constants.INTENT_KEY_VEHICLE_IGNITION_STATUS, getLastLocation?.ignitionStat)
        intent.putExtra(Constants.INTENT_KEY_VEHICLE_DRIVEN_TODAY, getLastLocation?.totalDistance)
        intent.putExtra(Constants.INTENT_KEY_VEHICLE_LAST_CONTACT_DATE, getLastLocation?.currentDate)
        intent.putExtra(Constants.INTENT_KEY_VEHICLE_LAST_CONTACT_TIME, getLastLocation?.currentTime)
        context.startActivity(intent)
    }

}