package org.digital.tracking.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.Gson
import com.mobitra.tracking.ReceiveLocationSubscription
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.OkHttpClient
import okhttp3.Request
import org.digital.tracking.R
import org.digital.tracking.bindingAdapter.*
import org.digital.tracking.databinding.ActivityLiveLocationBinding
import org.digital.tracking.maps.MapUtils
import org.digital.tracking.model.MapData
import org.digital.tracking.repository.VehicleRepository
import org.digital.tracking.utils.*
import org.digital.tracking.viewModel.LiveLocationViewModel
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class LiveLocationActivity : BaseActivity(), OnMapReadyCallback {

    @Inject
    lateinit var fallbackLocation: LatLng
    private var vehicleImei: String = ""
    private var vehicleNumber: String = ""
    private var lastLatitude: Double? = null
    private var lastLongitude: Double? = null
    private var lastContactDate: String? = ""
    private var lastContactTime: String? = ""
    private var lastGsmSignal: Double? = 0.0
    private var lastGpsStatus = Constants.GPS_FIXED_STATE_OFF
    private var bearingLastLocation: LatLng? = null

    private var vehicleRunningStatus: String = ""
    private var vehicleStopStatusWithTime: String = ""
    private var vehicleSpeed: Double? = 0.0
    private var drivenToday: Double? = 0.0
    private var vehicleIgnitionStatus: Double? = 0.0
    private var markerIconDrawable = R.drawable.car_red

    private var deviceLastLat: Double = Constants.DEFAULT_INDIA_LAT
    private var deviceLastLong: Double = Constants.DEFAULT_INDIA_LONG

    private var latestUpdatedLatitude: Double? = null
    private var latestUpdatedLongitude: Double? = null

    @Inject
    @Named("mapsApiKey")
    lateinit var mapsApiKey: String
    private var googleMap: GoogleMap? = null
    private var originMarker: Marker? = null
    private var destinationMarker: Marker? = null
    private lateinit var binding: ActivityLiveLocationBinding
    private val viewModel by viewModels<LiveLocationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLiveLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vehicleImei = intent.getStringExtra(Constants.INTENT_KEY_VEHICLE_IMEI) ?: ""
        vehicleNumber = intent.getStringExtra(Constants.INTENT_KEY_VEHICLE_NUMBER) ?: ""
        deviceLastLat = intent.getDoubleExtra(Constants.INTENT_KEY_LAST_LAT, Constants.DEFAULT_INDIA_LAT)
        deviceLastLong = intent.getDoubleExtra(Constants.INTENT_KEY_LAST_LONG, Constants.DEFAULT_INDIA_LONG)
        vehicleSpeed = intent.getDoubleExtra(Constants.INTENT_KEY_VEHICLE_SPEED, 0.0)
        drivenToday = intent.getDoubleExtra(Constants.INTENT_KEY_VEHICLE_DRIVEN_TODAY, 0.0)
        vehicleIgnitionStatus = intent.getDoubleExtra(Constants.INTENT_KEY_VEHICLE_IGNITION_STATUS, 0.0)
        vehicleRunningStatus = intent.getStringExtra(Constants.INTENT_KEY_VEHICLE_STATUS) ?: ""
        vehicleStopStatusWithTime = intent.getStringExtra(Constants.INTENT_KEY_VEHICLE_STOP_STATUS) ?: ""
        lastContactDate = intent.getStringExtra(Constants.INTENT_KEY_VEHICLE_LAST_CONTACT_DATE) ?: ""
        lastContactTime = intent.getStringExtra(Constants.INTENT_KEY_VEHICLE_LAST_CONTACT_TIME) ?: ""
        lastGsmSignal = intent.getDoubleExtra(Constants.INTENT_KEY_VEHICLE_GSM_STATUS, 0.0)
        lastGpsStatus = intent.getIntExtra(Constants.INTENT_KEY_VEHICLE_GPS_STATUS, Constants.GPS_FIXED_STATE_OFF)

        val title = "${getString(R.string.live_tracking)} (${vehicleNumber})"
        initToolbar(binding.root, title)

        if (vehicleNumber.isEmpty()) {
            showToast(getString(R.string.error_message_invalid_vehicle_number))
            finish()
            return
        }

        fallbackLocation = LatLng(deviceLastLat, deviceLastLong)
        //Update latest lat-long
        latestUpdatedLatitude = deviceLastLat
        latestUpdatedLongitude = deviceLastLong

        viewModel.getLiveLocation(vehicleImei)
//        viewModel.pushDummyLocation(vehicleNumber)

        setupObserver()
        setupVehicleLastInfo()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        val camera = CameraUpdateFactory.newLatLng(fallbackLocation)
        val zoom = CameraUpdateFactory.zoomTo(Constants.MAP_ZOOM_LEVEL_CITY)
        val deviceLastLocation = LatLng(deviceLastLat, deviceLastLong)
        if (originMarker == null) {
//            val color = ContextCompat.getColor(this, R.color.colorPrimary)
            val makerIcon = BitmapHelper.vectorToBitmap(this, markerIconDrawable)
            val marker = MarkerOptions().position(deviceLastLocation).icon(makerIcon).title(vehicleNumber)
            originMarker = googleMap?.addMarker(marker)
        } else {
            originMarker?.position = deviceLastLocation
        }
//        originMarker?.showInfoWindow()
        googleMap?.apply {
            moveCamera(camera)
            animateCamera(zoom)
        }
    }

    private fun setupObserver() {
        viewModel.liveLocationError.observe(this) { errorMessage ->
            Timber.d("LiveLocation: error : $errorMessage")
            if (errorMessage.isNullOrEmpty()) {
                return@observe
            }
            showSnackBarLong(binding.root, errorMessage)
        }

        viewModel.newLocation.observe(this) { newLocation ->
            Timber.d("New Location[${newLocation.vehicleNum}] : ${newLocation.latitude},${newLocation.longitude}")
            //Update latest lat-long
            latestUpdatedLatitude = newLocation.latitude
            latestUpdatedLongitude = newLocation.longitude
            setupVehicleInfo(newLocation)
            if (lastLatitude == null || lastLongitude == null) {
                lastLatitude = newLocation.latitude
                lastLongitude = newLocation.longitude
                markFirstPosition(lastLatitude, lastLongitude)
            } else {
                originMarker?.remove()
                drawRoute(newLocation.latitude, newLocation.longitude)
            }
        }
    }

    private fun markFirstPosition(originLatitude: Double?, originLongitude: Double?) {
        try {
            val originLocation = LatLng(originLatitude!!, originLongitude!!)
            if (originMarker == null) {
                val originMarkerIcon = BitmapHelper.vectorToBitmap(this, markerIconDrawable)
                val originMarkerOptions = MarkerOptions().position(originLocation).icon(originMarkerIcon)
                originMarker = googleMap?.addMarker(originMarkerOptions)
            } else {
                originMarker?.position = originLocation
            }
            val camera = CameraUpdateFactory.newLatLng(originLocation)
            val zoom = CameraUpdateFactory.zoomTo(Constants.MAP_ZOOM_LEVEL_DRIVING)
            googleMap?.apply {
                moveCamera(camera)
                animateCamera(zoom)
            }
        } catch (exception: Exception) {
            Timber.d("Exception markFirstPosition : ${exception.printStackTrace()}")
        }
    }

    private fun drawRoute(destinationLatitude: Double?, destinationLongitude: Double?) {
        try {
            val originLocation = LatLng(lastLatitude!!, lastLongitude!!)
            val destinationLocation = LatLng(destinationLatitude!!, destinationLongitude!!)
            if (bearingLastLocation == null) {
                bearingLastLocation = originLocation
            }
            if (destinationMarker == null) {
                val destinationMarkerIcon = BitmapHelper.vectorToBitmap(this, markerIconDrawable)
                val destinationMarkerOptions =
                    MarkerOptions().position(destinationLocation).icon(destinationMarkerIcon).title(vehicleNumber)
                val bearing = MapUtils.getLocationBearing(bearingLastLocation, destinationLocation)
                destinationMarkerOptions.rotation(bearing)
                destinationMarkerOptions.anchor(MapUtils.MAP_MARKER_ANCHOR_CENTRE_X_AXIS, MapUtils.MAP_MARKER_ANCHOR_CENTRE_Y_AXIS)
                destinationMarker = googleMap?.addMarker(destinationMarkerOptions)
            } else {
                destinationMarker?.setAnchor(MapUtils.MAP_MARKER_ANCHOR_CENTRE_X_AXIS, MapUtils.MAP_MARKER_ANCHOR_CENTRE_Y_AXIS)
                destinationMarker?.rotation = MapUtils.getLocationBearing(bearingLastLocation, destinationLocation)
                MapUtils.moveMarkerSmoothly(destinationMarker, destinationLocation).start()
                val directionsUrl = getDirectionURL(originLocation, destinationLocation, mapsApiKey)
                GetDirection(directionsUrl).execute()
            }
            val markerPosition = destinationMarker?.position ?: destinationLocation
            googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(markerPosition, Constants.MAP_ZOOM_LEVEL_DRIVING))
//            googleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        } catch (exception: Exception) {
            Timber.d("Exception Draw Route : $exception")
        }
    }

    @SuppressLint("StaticFieldLeak")
    private inner class GetDirection(val url: String) : AsyncTask<Void, Void, List<List<LatLng>>>() {
        override fun doInBackground(vararg params: Void?): List<List<LatLng>> {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val data = response.body!!.string()
            val result = ArrayList<List<LatLng>>()
            try {
                val respObj = Gson().fromJson(data, MapData::class.java)
                val path = ArrayList<LatLng>()
                for (i in 0 until respObj.routes[0].legs[0].steps.size) {
                    path.addAll(decodePolyline(respObj.routes[0].legs[0].steps[i].polyline.points))
                }
                result.add(path)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return result
        }

        override fun onPostExecute(result: List<List<LatLng>>) {
            val lineoption = PolylineOptions()
            for (i in result.indices) {
                lineoption.addAll(result[i])
                lineoption.width(10f)
                lineoption.color(Color.GREEN)
                lineoption.geodesic(true)
            }
            googleMap?.addPolyline(lineoption)
        }
    }


    private fun getDirectionURL(origin: LatLng, dest: LatLng, secret: String): String {
        return "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}" +
                "&destination=${dest.latitude},${dest.longitude}" +
                "&sensor=false" +
                "&mode=driving" +
                "&key=$secret"
    }

    fun decodePolyline(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            val latLng = LatLng((lat.toDouble() / 1E5), (lng.toDouble() / 1E5))
            poly.add(latLng)
        }
        return poly
    }

    private fun setupVehicleLastInfo() {
        binding.vehicleInfo.vehicleNumber.text = vehicleNumber
        binding.vehicleInfo.addressTextView.text = getCompleteAddressString(deviceLastLat, deviceLastLong)
        binding.vehicleInfo.speedTextView.text = vehicleSpeed.getSpeedString()
        binding.vehicleInfo.movedKmTextView.text = drivenToday.getDistanceString()
        binding.vehicleInfo.lastContact.text = getReadableDateAndTime(lastContactDate, lastContactTime)
        binding.vehicleInfo.signalIcon.setVehicleSignalColor(lastGsmSignal)
        binding.vehicleInfo.gpsIcon.setGpsIconColor(lastGpsStatus)

        //Ignition Status
        if (vehicleIgnitionStatus?.toInt() == Constants.IGNITION_STAT_ON) {
            binding.vehicleInfo.ignitionStatus.setIgnitionStatusIconColor(isIgnitionOn = true)
        } else {
            binding.vehicleInfo.ignitionStatus.setIgnitionStatusIconColor(isIgnitionOn = false)
        }

        //Vehicle Status
        val vehicleStatus: String = vehicleRunningStatus

        binding.vehicleInfo.vehicleNumber.setVehicleNumberColor(vehicleStatus)
        binding.vehicleInfo.vehicleStatusText.setVehicleStatusText(vehicleStatus)
        binding.vehicleInfo.vehicleStatusIcon.setVehicleStatusIconColor(vehicleStatus)
        binding.vehicleInfo.setVehicleIconStatus.setVehicleIconStatus(vehicleStatus)
        if (vehicleStopStatusWithTime.isNotEmpty()) {
            binding.vehicleInfo.vehicleStatusText.text = vehicleStopStatusWithTime
        }

        if (vehicleStatus.contains(Constants.VEHICLE_STATUS_KEY_IDLE, ignoreCase = true)) {
            markerIconDrawable = R.drawable.car_red
        } else if (vehicleStatus.contains(Constants.VEHICLE_STATUS_KEY_RUNNING, ignoreCase = true)) {
            markerIconDrawable = R.drawable.car_green
        } else if (vehicleStatus.contains(Constants.VEHICLE_STATUS_KEY_STOP, ignoreCase = true)) {
            markerIconDrawable = R.drawable.car_red
        } else {
            markerIconDrawable = R.drawable.car_red
        }

        binding.vehicleInfo.replayIcon.setOnClickListener {
            val intent = Intent(this, HistoryViewActivity::class.java)
            intent.putExtra(Constants.INTENT_KEY_TITLE, getString(R.string.title_replay_view))
            intent.putExtra(Constants.INTENT_KEY_VEHICLE_IMEI, vehicleImei)
            intent.putExtra(Constants.INTENT_KEY_VEHICLE_NUMBER, VehicleRepository.getVehicleNumber(vehicleImei))
            intent.putExtra(Constants.INTENT_KEY_LIVE_LOCATIONS_TYPE, Constants.INTENT_KEY_LIVE_LOCATIONS_TYPE_REPLAY)
            navigateToActivity(intent)
        }

        binding.vehicleInfo.shareIcon.setOnClickListener {
            MapUtils.shareLocationLink(this, latestUpdatedLatitude, latestUpdatedLongitude)
        }

    }

    private fun setupVehicleInfo(vehicle: ReceiveLocationSubscription.ReceiveLocation) {
        binding.vehicleInfo.vehicleNumber.text = vehicleNumber
        binding.vehicleInfo.addressTextView.text = getCompleteAddressString(vehicle.latitude, vehicle.longitude)
        binding.vehicleInfo.lastContact.text = getReadableDateAndTime(vehicle.currentDate, vehicle.currentTime)
        binding.vehicleInfo.speedTextView.text = vehicle.speed.getSpeedString()
        binding.vehicleInfo.signalIcon.setVehicleSignalColor(vehicle.gsmSigStr)
        binding.vehicleInfo.gpsIcon.setGpsIconColor(vehicle.gpsFixState)

        //Ignition Status
        if (vehicle.ignitionStat?.toInt() == Constants.IGNITION_STAT_ON) {
            binding.vehicleInfo.ignitionStatus.setIgnitionStatusIconColor(isIgnitionOn = true)
        } else {
            binding.vehicleInfo.ignitionStatus.setIgnitionStatusIconColor(isIgnitionOn = false)
        }

        //Vehicle Status
        val speedInt: Int = vehicle.speed?.toInt() ?: 0
        val ignitionInt: Int = vehicle.ignitionStat?.toInt() ?: Constants.IGNITION_STAT_OFF
        var vehicleStatus: String = Constants.VEHICLE_STATUS_KEY_UNKNOWN
        if (ignitionInt == Constants.IGNITION_STAT_ON && speedInt <= 5) {
            vehicleStatus = Constants.VEHICLE_STATUS_KEY_IDLE
        } else if (ignitionInt == Constants.IGNITION_STAT_OFF) {
            vehicleStatus = Constants.VEHICLE_STATUS_KEY_STOP
        } else if (ignitionInt == Constants.IGNITION_STAT_ON && speedInt > 5) {
            vehicleStatus = Constants.VEHICLE_STATUS_KEY_RUNNING
        }

        binding.vehicleInfo.vehicleNumber.setVehicleNumberColor(vehicleStatus)
        binding.vehicleInfo.vehicleStatusText.setVehicleStatusText(vehicleStatus)
        binding.vehicleInfo.setVehicleIconStatus.setVehicleIconStatus(vehicleStatus)
        binding.vehicleInfo.vehicleStatusIcon.setVehicleStatusIconColor(vehicleStatus)

        if (vehicleStatus.contains(Constants.VEHICLE_STATUS_KEY_IDLE, ignoreCase = true)) {
            markerIconDrawable = R.drawable.car_red
        } else if (vehicleStatus.contains(Constants.VEHICLE_STATUS_KEY_RUNNING, ignoreCase = true)) {
            markerIconDrawable = R.drawable.car_green
        } else if (vehicleStatus.contains(Constants.VEHICLE_STATUS_KEY_STOP, ignoreCase = true)) {
            markerIconDrawable = R.drawable.car_red
        } else {
            markerIconDrawable = R.drawable.car_red
        }


        binding.vehicleInfo.replayIcon.setOnClickListener {
            val intent = Intent(this, HistoryViewActivity::class.java)
            intent.putExtra(Constants.INTENT_KEY_TITLE, getString(R.string.title_replay_view))
            intent.putExtra(Constants.INTENT_KEY_VEHICLE_IMEI, vehicle.tags?.IMEINumber)
            intent.putExtra(Constants.INTENT_KEY_VEHICLE_NUMBER, VehicleRepository.getVehicleNumber(vehicle.tags?.IMEINumber ?: ""))
            intent.putExtra(Constants.INTENT_KEY_LIVE_LOCATIONS_TYPE, Constants.INTENT_KEY_LIVE_LOCATIONS_TYPE_REPLAY)
            navigateToActivity(intent)
        }

    }

}