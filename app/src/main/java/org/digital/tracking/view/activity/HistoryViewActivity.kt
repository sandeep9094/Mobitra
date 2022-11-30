package org.digital.tracking.view.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.*
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import com.mobitra.tracking.LocationsQuery
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.OkHttpClient
import okhttp3.Request
import org.digital.tracking.R
import org.digital.tracking.databinding.ActivityHistoryViewBinding
import org.digital.tracking.enum.ReplayViewSpeed
import org.digital.tracking.maps.MapUtils
import org.digital.tracking.model.ApiResult
import org.digital.tracking.model.MapData
import org.digital.tracking.repository.VehicleRepository
import org.digital.tracking.utils.*
import org.digital.tracking.viewModel.HistoryLocationViewModel
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named


@AndroidEntryPoint
class HistoryViewActivity : BaseActivity(), OnMapReadyCallback, View.OnClickListener {

    @Inject
    lateinit var fallbackLocation: LatLng
    private var vehicleNumber: String = ""
    private var lastLatitude: Double? = null
    private var lastLongitude: Double? = null
    private var isReplayRunning: Boolean = false
    private var destinationMarker: Marker? = null
    private var mapsZoomLevel = Constants.MAP_ZOOM_LEVEL_BTW_CITY_AND_STREETS_14
    private var liveLocationsType = Constants.INTENT_KEY_LIVE_LOCATIONS_TYPE_HISTORY
    private var bearingLastLocation: LatLng? = null

    @Inject
    @Named("mapsApiKey")
    lateinit var mapsApiKey: String
    private var googleMap: GoogleMap? = null
    private lateinit var binding: ActivityHistoryViewBinding
    private val viewModel by viewModels<HistoryLocationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val title = intent.getStringExtra(Constants.INTENT_KEY_TITLE) ?: getString(R.string.title_history_view)
        liveLocationsType =
            intent.getStringExtra(Constants.INTENT_KEY_LIVE_LOCATIONS_TYPE) ?: Constants.INTENT_KEY_LIVE_LOCATIONS_TYPE_HISTORY
        initToolbar(binding.root, title, rightIconVisibility = true) { rightIcon ->
            vehicleListDialog()
        }

        initUiComponents()
        setupObserver()
        viewModel.getVehicleList()
    }

    private fun initUiComponents() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if (liveLocationsType == Constants.INTENT_KEY_LIVE_LOCATIONS_TYPE_REPLAY) {
            setupReplayMode()
        } else {
            binding.replayLocationMenuLayout.rootLayout.makeGone()
        }
        if (intent.hasExtra(Constants.INTENT_KEY_VEHICLE_IMEI)) {
            //Opened with selected vehicle
            val vehicleImei = intent.getStringExtra(Constants.INTENT_KEY_VEHICLE_IMEI)
            val vehicleNumber = intent.getStringExtra(Constants.INTENT_KEY_VEHICLE_NUMBER)
            vehicleImei?.let {
                Timber.d("VehicleImei : ${vehicleImei}")
                fetchVehicleLocations(vehicleImei, vehicleNumber ?: "")
                return
            }
        }

        org.digital.tracking.utils.showSnackBar(binding.root, "Select vehicle from filters")
    }

    private fun setupReplayMode() {

        binding.speedViewButton.makeVisible()
        binding.replayLocationMenuLayout.rootLayout.makeVisible()

        binding.speedViewButton.setOnClickListener(this)
        binding.replayLocationMenuLayout.playPauseButton.setOnClickListener(this)

        binding.replayLocationMenuLayout.replaySeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, p2: Boolean) {
                if (progress == seekBar?.max) {
                    //If Auto complete replay, then pause
                    isReplayRunning = false
                    viewModel.pauseReplayLocations()
                    updateReplayPlayerStatus()
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                Timber.d("Replay SeekBar Progress : ${seekBar?.progress}")
                //Pause current replay
                isReplayRunning = false
                viewModel.pauseReplayLocations()
                updateReplayPlayerStatus()
            }

        })
    }


    override fun onClick(view: View?) {
        when (view?.id) {
            binding.replayLocationMenuLayout.playPauseButton.id -> {
                if (isReplayRunning) {
                    isReplayRunning = false
                    viewModel.pauseReplayLocations()
                } else {
                    isReplayRunning = true
                    val replayLocationIndex = binding.replayLocationMenuLayout.replaySeekBar.progress
                    viewModel.resumeReplayLocations(replayLocationIndex)
                }
                updateReplayPlayerStatus()
            }
            binding.speedViewButton.id -> {
                showReplaySpeedDialog()
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        val camera = CameraUpdateFactory.newLatLng(fallbackLocation)
        val zoom = CameraUpdateFactory.zoomTo(Constants.MAP_ZOOM_LEVEL_CONTINENT)
        googleMap?.apply {
            moveCamera(camera)
            animateCamera(zoom)
        }
    }

    private fun vehicleListDialog() {
        val vehicleList = ArrayList<String>()
        val vehicleMap = VehicleRepository.getVehicleNumberHashMap()
        val vehicleImeiList = ArrayList(vehicleMap.keys)
        vehicleMap.forEach {
            vehicleList.add(it.value)
        }
        var dialog: AlertDialog? = null
        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, vehicleList)
        val builder = AlertDialog.Builder(this)
        val dialogLayout = layoutInflater.inflate(R.layout.dialog_report_filter, null)
        builder.setView(dialogLayout)

        val fromDateEditText = dialogLayout.findViewById<EditText>(R.id.fromDateEditText)
        val toDateEditText = dialogLayout.findViewById<EditText>(R.id.toDateEditText)

        fromDateEditText.setOnClickListener {
            showDatePicker(fromDateEditText)
        }

        toDateEditText.setOnClickListener {
            showDatePicker(toDateEditText)
        }

        val vehicleListView = dialogLayout.findViewById<ListView>(R.id.vehicleList)
        vehicleListView.adapter = arrayAdapter
        vehicleListView.setOnItemClickListener { adapterView, view, position, item ->
            if (fromDateEditText.text.toString().isEmpty() || toDateEditText.text.toString().isEmpty()) {
                showToast("Please select valid date!")
                return@setOnItemClickListener
            }
            val fromDate = fromDateEditText.text.toString().formatDateForServer(isStartDate = true)
            val toDate = toDateEditText.text.toString().formatDateForServer()
            Timber.d("VehicleFilter  Selected: ${vehicleList[position]}, from: $fromDate, to: $toDate")
            val selectedVehicleImei = vehicleImeiList[position]
            fetchVehicleLocations(selectedVehicleImei, vehicleList[position], fromDate, toDate)
            dialog?.dismiss()
        }
        dialog = builder.create()
        dialog.show()
    }


    private fun fetchVehicleLocations(
        imeiNumber: String,
        vehicleNumber: String,
        fromDate: String = getYesterdayDate(),
        toDate: String = getTodayDate()
    ) {
        googleMap?.clear()
        destinationMarker = null
        this.vehicleNumber = vehicleNumber
        Timber.d("VehicleIMEINumber : $imeiNumber")
        viewModel.getLocations(imeiNumber, fromDate, toDate)
    }

    private fun setupObserver() {
        //Show vehicle list name in filters
        viewModel.vehicleNameListResult.observe(this) {
            //TODO Show vehicle list name in filters
        }

        //Fetch all history
        //TODO finish all history locations in total 1 minute timestamp
        viewModel.historyLocationsResult.observe(this) {
            when (it) {
                is ApiResult.Success -> {
                    processLocations(it.data)
                }
                else -> {
                    Timber.d("Unable to receive History Locations")
                }
            }
        }

        //Move only vehicle to coordinates
        viewModel.vehicleHistoryMovingLocation.observe(this) { newLocation ->
            if (lastLatitude == null || lastLongitude == null) {
                lastLatitude = newLocation?.latitude
                lastLongitude = newLocation?.longitude
                markFirstPosition(lastLatitude, lastLongitude)
            } else {
                drawRoute(newLocation?.latitude, newLocation?.longitude, enableRoute = true)
            }
            if (liveLocationsType == Constants.INTENT_KEY_LIVE_LOCATIONS_TYPE_REPLAY) {
                val currentDate = getReadableDate(newLocation?.currentDate, newLocation?.currentTime)
                val currentTime = getReadableTime(newLocation?.currentDate, newLocation?.currentTime)

                binding.replayLocationMenuLayout.dateTextView.text = currentDate
                binding.replayLocationMenuLayout.timeTextView.text = currentTime
                binding.replayLocationMenuLayout.speedTextView.text = newLocation?.speed.toString()
            }
        }

        // This is to update progress in replay seekbar on new location update
        viewModel.replayLocationCurrentIndex.observe(this) { index ->
            binding.replayLocationMenuLayout.replaySeekBar.progress = index
        }

    }

    private fun markFirstPosition(originLatitude: Double?, originLongitude: Double?) {
        try {
            val originLocation = LatLng(originLatitude!!, originLongitude!!)
            val originMarkerIcon = BitmapHelper.vectorToBitmap(this, R.drawable.car_red)
            val originMarker = MarkerOptions().position(originLocation).icon(originMarkerIcon)
            googleMap?.addMarker(originMarker)
            val camera = CameraUpdateFactory.newLatLng(originLocation)
            val zoom = CameraUpdateFactory.zoomTo(mapsZoomLevel)
            googleMap?.apply {
                moveCamera(camera)
                animateCamera(zoom)
            }
        } catch (exception: Exception) {
            Timber.d("Exception markFirstPosition : ${exception.printStackTrace()}")
        }
    }

    private fun drawRoute(destinationLatitude: Double?, destinationLongitude: Double?, enableRoute: Boolean = false) {
        try {
            val originLocation = LatLng(lastLatitude!!, lastLongitude!!)
            val destinationLocation = LatLng(destinationLatitude!!, destinationLongitude!!)
            if (bearingLastLocation == null) {
                bearingLastLocation = originLocation
            }
            if (destinationMarker == null) {
                val destinationMarkerIcon = BitmapHelper.vectorToBitmap(this, R.drawable.car_red)
                val destinationMarkerOptions =
                    MarkerOptions().position(destinationLocation).icon(destinationMarkerIcon).title(vehicleNumber)
                val bearing = MapUtils.getLocationBearing(bearingLastLocation, destinationLocation)
                destinationMarkerOptions.rotation(bearing)
                destinationMarkerOptions.anchor(MapUtils.MAP_MARKER_ANCHOR_CENTRE_X_AXIS, MapUtils.MAP_MARKER_ANCHOR_CENTRE_Y_AXIS)
                destinationMarker = googleMap?.addMarker(destinationMarkerOptions)
            } else {
                destinationMarker?.setAnchor(MapUtils.MAP_MARKER_ANCHOR_CENTRE_X_AXIS, MapUtils.MAP_MARKER_ANCHOR_CENTRE_Y_AXIS)
                destinationMarker?.rotation = MapUtils.getLocationBearing(bearingLastLocation, destinationLocation)
//                destinationMarker?.position = destinationLocation
                val markerAnimationDuration = viewModel.replayViewSpeed
                MapUtils.moveMarkerSmoothly(destinationMarker, destinationLocation, markerAnimationDuration).start()
            }
            destinationMarker?.showInfoWindow()
            bearingLastLocation = destinationLocation

            if (enableRoute) {
                val directionsUrl = getDirectionURL(originLocation, destinationLocation, mapsApiKey)
                GetDirection(directionsUrl).execute()
            }
            googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(destinationLocation, mapsZoomLevel))
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

    private fun processLocations(locations: List<LocationsQuery.Location?>) {
        Timber.d("processLocations------------------------")
        if (liveLocationsType == Constants.INTENT_KEY_LIVE_LOCATIONS_TYPE_HISTORY) {
            //TODO Current time add new param, time in graphql query, and sort by that
//                    historyLocations.sortedBy { it.currentTime }
            if (locations.size >= 2) {
                lastLatitude = locations.first()?.latitude
                lastLongitude = locations.first()?.longitude
                drawRoute(locations.last()?.latitude, locations.last()?.longitude, enableRoute = true)
            }
            viewModel.pushLocations(locations)
        } else if (liveLocationsType == Constants.INTENT_KEY_LIVE_LOCATIONS_TYPE_REPLAY) {
            Timber.d("processLocations: Replay----------")
            if (binding.replayLocationMenuLayout.replaySeekBar.max == 0) {
                //For first time update replay max with total size of locations
                binding.replayLocationMenuLayout.replaySeekBar.max = locations.size - 1
            }
            if (locations.size >= 2) {
                lastLatitude = locations.first()?.latitude
                lastLongitude = locations.first()?.longitude
                drawRoute(locations.last()?.latitude, locations.last()?.longitude, enableRoute = true)
            }
            //Update UI
            isReplayRunning = true
            updateReplayPlayerStatus(enableButton = true)
            //Update data
            viewModel.pushLocations(locations)
            viewModel.replayLocations.value = locations
        }
    }

    private fun updateReplayPlayerStatus(enableButton: Boolean = false) {
        if (enableButton) {
            binding.replayLocationMenuLayout.playPauseButton.isClickable = true
        }
        if (isReplayRunning) {
            binding.replayLocationMenuLayout.playPauseButton.setDrawable(this, R.drawable.ic_baseline_pause_circle_24_grey)
        } else {
            binding.replayLocationMenuLayout.playPauseButton.setDrawable(this, R.drawable.ic_baseline_play_circle_24_grey)
        }
    }

    private fun showReplaySpeedDialog() {
        val replaySpeedList: ArrayList<String> = ReplayViewSpeed.getSpeedList()
        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, replaySpeedList)
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Replay Speed")

        builder.setSingleChoiceItems(arrayAdapter, -1) { dialog, which ->
            binding.replaySpeedText.text = replaySpeedList[which]
            Timber.d("Speed Selected : ${replaySpeedList[which]}")
            updateReplaySpeed(which)
            dialog.dismiss()
        }
        builder.create().show()
    }

    private fun updateReplaySpeed(index: Int) {
        when (index) {
            0 -> viewModel.replayViewSpeed = ReplayViewSpeed.SLOWER_1X
            1 -> viewModel.replayViewSpeed = ReplayViewSpeed.SLOW_2X
            2 -> viewModel.replayViewSpeed = ReplayViewSpeed.NORMAL_3X
            3 -> viewModel.replayViewSpeed = ReplayViewSpeed.FAST_4X
            4 -> viewModel.replayViewSpeed = ReplayViewSpeed.FASTER_5X
//            5 -> viewModel.replayViewSpeed = ReplayViewSpeed.SUPER_FAST_10X
        }
    }

}
