package org.digital.tracking.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollographql.apollo3.ApolloClient
import com.mobitra.tracking.LastLocationsQuery
import dagger.hilt.android.AndroidEntryPoint
import org.digital.tracking.R
import org.digital.tracking.adapter.VehicleListAdapter
import org.digital.tracking.databinding.FragmentListBinding
import org.digital.tracking.maps.MapUtils
import org.digital.tracking.model.ApiResult
import org.digital.tracking.repository.VehicleRepository
import org.digital.tracking.utils.*
import org.digital.tracking.view.activity.HistoryViewActivity
import org.digital.tracking.view.activity.LiveLocationActivity
import org.digital.tracking.viewModel.HomeViewModel
import javax.inject.Inject

@AndroidEntryPoint
class ListFragment : BaseFragment(), VehicleListAdapter.Listener {

    @Inject
    lateinit var apolloClient: ApolloClient
    private lateinit var binding: FragmentListBinding
    private val viewModel by activityViewModels<HomeViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHomeToolbar(binding.root)

        if (VehicleRepository.getVehicleList().isEmpty()) {
            viewModel.fetchAllVehicles()
        } else {
            initVehicleList()
            viewModel.fetchAllVehicles()
        }

        setupObserver()
    }

    override fun onVehicleReplayClick(vehicle: LastLocationsQuery.LastLocation?) {
        val intent = Intent(context, HistoryViewActivity::class.java)
        intent.putExtra(Constants.INTENT_KEY_TITLE, getString(R.string.title_replay_view))
        intent.putExtra(Constants.INTENT_KEY_VEHICLE_NUMBER, VehicleRepository.getVehicleNumber(vehicle?.IMEINumber ?: ""))
        intent.putExtra(Constants.INTENT_KEY_VEHICLE_IMEI, vehicle?.IMEINumber)
        intent.putExtra(Constants.INTENT_KEY_LIVE_LOCATIONS_TYPE, Constants.INTENT_KEY_LIVE_LOCATIONS_TYPE_REPLAY)
        context.navigateToActivity(intent)
    }

    override fun onVehicleShareLocationClick(vehicle: LastLocationsQuery.LastLocation?) {
        context?.let {
            MapUtils.shareLocationLink(it, vehicle?.latitude, vehicle?.longitude)
        }
    }


    override fun onVehicleItemClick(vehicle: LastLocationsQuery.LastLocation?, vehicleStatus: String, statusTextViewString: String) {
        openLiveLocation(vehicle, vehicleStatus, statusTextViewString)
    }

    private fun openLiveLocation(vehicle: LastLocationsQuery.LastLocation?, vehicleStatus: String, statusTextViewString: String = "") {
        if (vehicle == null) {
            showError()
            return
        }
        val intent = Intent(context, LiveLocationActivity::class.java)
        intent.putExtra(Constants.INTENT_KEY_VEHICLE_IMEI, vehicle.IMEINumber)
        intent.putExtra(Constants.INTENT_KEY_VEHICLE_NUMBER, VehicleRepository.getVehicleNumber(vehicle.IMEINumber ?: ""))
        intent.putExtra(Constants.INTENT_KEY_LAST_LAT, vehicle.latitude)
        intent.putExtra(Constants.INTENT_KEY_LAST_LONG, vehicle.longitude)
        intent.putExtra(Constants.INTENT_KEY_VEHICLE_SPEED, vehicle.speed)
        intent.putExtra(Constants.INTENT_KEY_VEHICLE_STATUS, vehicleStatus)
        intent.putExtra(Constants.INTENT_KEY_VEHICLE_STOP_STATUS, statusTextViewString)
        intent.putExtra(Constants.INTENT_KEY_VEHICLE_IGNITION_STATUS, vehicle.ignitionStat)
        intent.putExtra(Constants.INTENT_KEY_VEHICLE_DRIVEN_TODAY, vehicle.totalDistance)
        intent.putExtra(Constants.INTENT_KEY_VEHICLE_LAST_CONTACT_DATE, vehicle.currentDate)
        intent.putExtra(Constants.INTENT_KEY_VEHICLE_LAST_CONTACT_TIME, vehicle.currentTime)
        intent.putExtra(Constants.INTENT_KEY_VEHICLE_GPS_STATUS, vehicle.gpsFixState)
        intent.putExtra(Constants.INTENT_KEY_VEHICLE_GSM_STATUS, vehicle.gsmSigStr)

        context?.startActivity(intent)
    }

    private fun setupObserver() {
        viewModel.vehicleList.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResult.Loading -> {
//                    binding.progress.makeVisible()
//                    binding.error.makeGone()
                    binding.recyclerView.makeVisible()
                }
                is ApiResult.Error -> {
                    showError()
                }
                is ApiResult.Success -> {
                    val list = it.data
                    if (list.isEmpty()) {
                        showError()
                        return@observe
                    }
                    VehicleRepository.setVehicleList(list)
                    initVehicleList()
                    binding.progress.makeGone()
                    binding.error.makeGone()
                    binding.recyclerView.makeVisible()
                }
            }
        }
    }

    private fun showError() {
        binding.progress.makeGone()
        binding.error.makeVisible()
        binding.recyclerView.makeGone()
    }


    private fun initVehicleList() {
        val vehicleList = VehicleRepository.getVehicleList()
        binding.recyclerView.adapter = VehicleListAdapter(vehicleList, this, apolloClient)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
    }

}