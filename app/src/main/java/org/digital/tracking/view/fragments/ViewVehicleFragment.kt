package org.digital.tracking.view.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mobitra.tracking.LastLocationsQuery
import dagger.hilt.android.AndroidEntryPoint
import org.digital.tracking.R
import org.digital.tracking.bindingAdapter.setVehicleStatus
import org.digital.tracking.databinding.FragmentViewVehicleBinding
import org.digital.tracking.model.ApiResult
import org.digital.tracking.utils.*
import org.digital.tracking.viewModel.VehicleViewModel
import timber.log.Timber

@AndroidEntryPoint
class ViewVehicleFragment : BaseFragment(), OnMapReadyCallback {

    private var vehicle: LastLocationsQuery.LastLocation? = null
    private var lastLocation: LastLocationsQuery.LastLocation? = null

    private var map: GoogleMap? = null
    private lateinit var binding: FragmentViewVehicleBinding
    private val viewModel by activityViewModels<VehicleViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_vehicle, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vehicle = viewModel.selectedVehicle

        if (vehicle == null) {
            context.showToast("Something Went Wrong!")
            findNavController().navigateUp()
            return
        }
        vehicle?.let {
            initToolbar(binding.root, it.vehicleNum ?: "")
            setVehicleUI(it)
            Handler(Looper.getMainLooper()).postDelayed({
                binding.map.onCreate(savedInstanceState)
                binding.map.onResume()
            }, 100)
        }

        viewModel.fetchLocation(vehicle?.vehicleNum ?: "")
        setupObserver()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val vehicleLat = lastLocation?.latitude ?: Constants.DEFAULT_INDIA_LAT
        val vehicleLong = lastLocation?.longitude ?: Constants.DEFAULT_INDIA_LONG
        val vehicleLocation = LatLng(vehicleLat.toDouble(), vehicleLong.toDouble())
        context?.let {
            val color = ContextCompat.getColor(it, R.color.colorPrimary)
            val makerIcon = BitmapHelper.vectorToBitmap(it, R.drawable.ic_baseline_directions_car_24, color)
            val marker = MarkerOptions().position(vehicleLocation).icon(makerIcon).title(vehicle?.vehicleNum ?: "Unknown Vehicle")
            val camera = CameraUpdateFactory.newLatLng(vehicleLocation)
            val zoom = CameraUpdateFactory.zoomTo(10f)
            map?.apply {
                addMarker(marker)
                moveCamera(camera)
                animateCamera(zoom)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        map?.clear()
    }

    private fun setupObserver() {
        viewModel.lastLocationApiResult.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResult.Loading -> {
                    Timber.d("Last Location Loading---------")
                }
                is ApiResult.Error -> {
                    showFetchLocationError()
                }
                is ApiResult.Success -> {
                    Timber.d("Last Location Success---------")
                    Timber.d("Last Location : ${it.data[0]}")
                    val location: LastLocationsQuery.LastLocation? = it.data.get(0)
                    if (location == null) {
                        showFetchLocationError()
                        return@observe
                    }
                    binding.map.getMapAsync(this)
                    lastLocation = location
                }
            }
        }
    }

    private fun showFetchLocationError() {
        binding.map.makeInvisible()
        showToast(R.string.error_message_failed_to_fetch_location)
    }

    private fun setVehicleUI(vehicle: LastLocationsQuery.LastLocation) {
//        binding.addressTextView.text = binding.addressTextView.context.getCompleteAddressString(vehicle.latitude, vehicle.longitude)
//        binding.speedTextView.text = vehicle.speed.getSpeedString()
//        if (vehicle.gpsFixState == Constants.GPS_FIX_STATE_IS_PARKED) {
//            binding.statusIcon.setVehicleStatus("idle")
//            binding.vehicleStatusText.text = "Idle"
//            binding.parkingStatus.setParkingStatus(isVehicleParking = true)
//        } else {
//            binding.statusIcon.setVehicleStatus("moving")
//            binding.vehicleStatusText.text = "Running"
//            binding.parkingStatus.setParkingStatus(isVehicleParking = false)
//        }
    }

}