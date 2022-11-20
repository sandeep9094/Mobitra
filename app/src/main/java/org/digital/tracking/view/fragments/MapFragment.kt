package org.digital.tracking.view.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.ktx.addCircle
import com.mobitra.tracking.LastLocationsQuery
import dagger.hilt.android.AndroidEntryPoint
import org.digital.tracking.R
import org.digital.tracking.databinding.FragmentMapBinding
import org.digital.tracking.maps.Place
import org.digital.tracking.maps.PlaceRenderer
import org.digital.tracking.maps.PlacesReader
import org.digital.tracking.model.ApiResult
import org.digital.tracking.repository.VehicleRepository
import org.digital.tracking.utils.*
import org.digital.tracking.viewModel.HomeViewModel
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MapFragment : BaseFragment(), OnMapReadyCallback {

    @Inject
    lateinit var fallbackLocation: LatLng
    private lateinit var binding: FragmentMapBinding
    private lateinit var map: GoogleMap
    private var fragmentSavedInstanceState: Bundle? = null
    private val viewModel by activityViewModels<HomeViewModel>()

    private val places: List<Place> by lazy {
        PlacesReader().read()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMapBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHomeToolbar(binding.root)
        fragmentSavedInstanceState = savedInstanceState

        if (VehicleRepository.getVehicleList().isEmpty()) {
            viewModel.fetchAllVehicles()
        } else {
            showMap()
        }
        setupObserver()

    }

    private fun showMap() {
        Handler(Looper.getMainLooper()).postDelayed({
            binding.progressView.makeGone()
            binding.map.makeVisible()
            binding.map.onCreate(fragmentSavedInstanceState)
            binding.map.onResume()
            binding.map.getMapAsync(this)
        }, 250)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        if (places.isEmpty()) {
            //Move to fallback Location
            val camera = CameraUpdateFactory.newLatLng(fallbackLocation)
            val zoom = CameraUpdateFactory.zoomTo(Constants.MAP_ZOOM_LEVEL_CONTINENT)
            map.apply {
                moveCamera(camera)
                animateCamera(zoom)
            }
            return
        }
        try {
            addClusteredMarkers(googleMap)
            val bounds = LatLngBounds.builder()
            places.forEach { bounds.include(LatLng(it.lat, it.long)) }
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 20))
        } catch (exception: Exception) {

        }
    }

    /**
     * Adds markers to the map with clustering support.
     */
    private fun addClusteredMarkers(googleMap: GoogleMap) {
        // Create the ClusterManager class and set the custom renderer
        activity?.let {
            val clusterManager = ClusterManager<Place>(it, googleMap)
            clusterManager.renderer = PlaceRenderer(it, googleMap, clusterManager)

            // Set custom info window adapter
//        clusterManager.markerCollection.setInfoWindowAdapter(MarkerInfoWindowAdapter(this))

            // Add the places to the ClusterManager
            clusterManager.addItems(places)
            clusterManager.cluster()

            // Show polygon
            clusterManager.setOnClusterItemClickListener { item ->
                addCircle(googleMap, item)
                Timber.d("ClusterManger : itemClick : $item")
                return@setOnClusterItemClickListener false
            }

            // When the camera starts moving, change the alpha value of the marker to translucent
            googleMap.setOnCameraMoveStartedListener {
                clusterManager.markerCollection.markers.forEach { it.alpha = 0.3f }
                clusterManager.clusterMarkerCollection.markers.forEach { it.alpha = 0.3f }
            }

            googleMap.setOnCameraIdleListener {
                // When the camera stops moving, change the alpha value back to opaque
                clusterManager.markerCollection.markers.forEach { it.alpha = 1.0f }
                clusterManager.clusterMarkerCollection.markers.forEach { it.alpha = 1.0f }

                // Call clusterManager.onCameraIdle() when the camera stops moving so that re-clustering
                // can be performed when the camera stops moving
                clusterManager.onCameraIdle()
            }
        }
    }

    /**
     * Adds a [Circle] around the provided [item]
     */
    private var circle: Circle? = null
    private fun addCircle(googleMap: GoogleMap, item: Place) {
        context?.let {
            circle?.remove()
            circle = googleMap.addCircle {
                center(LatLng(item.lat, item.long))
                radius(1000.0)
                fillColor(ContextCompat.getColor(it, R.color.colorPrimary))
                strokeColor(ContextCompat.getColor(it, R.color.colorPrimary))
            }
        }
    }

    private fun setupObserver() {
        viewModel.vehicleList.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResult.Loading -> {
                }
                is ApiResult.Error -> {
                    showFetchLocationError()
                }
                is ApiResult.Success -> {
                    val list = it.data
                    VehicleRepository.setVehicleList(list)
                    showMap()
                }
            }
        }
//        viewModel.allVehicleLocationApiResult.observe(viewLifecycleOwner) {
//            when (it) {
//                is ApiResult.Loading -> {
//                    Timber.d("Last Location Loading---------")
//                }
//                is ApiResult.Error -> {
//                    showFetchLocationError()
//                }
//                is ApiResult.Success -> {
//                    showMap()
//                }
//            }
//        }
    }

    private fun showFetchLocationError() {
        binding.map.makeInvisible()
        showToast(R.string.error_message_failed_to_fetch_location)
    }

}