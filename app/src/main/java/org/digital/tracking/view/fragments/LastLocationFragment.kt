package org.digital.tracking.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloException
import com.google.gson.Gson
import com.mobitra.tracking.LocationsQuery
import dagger.hilt.android.AndroidEntryPoint
import org.digital.tracking.R
import org.digital.tracking.adapter.LastLocationReportAdapter
import org.digital.tracking.databinding.FragmentLastLocationBinding
import org.digital.tracking.model.LastLocationReport
import org.digital.tracking.repository.VehicleRepository
import org.digital.tracking.repository.cache.UserCacheManager
import org.digital.tracking.utils.*
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class LastLocationFragment : ReportsBaseFragment() {

    @Inject
    lateinit var apolloClient: ApolloClient
    private lateinit var binding: FragmentLastLocationBinding
    private var lastLocationsReport: ArrayList<LastLocationReport> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLastLocationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbarReports(binding.root, getString(R.string.title_last_location_report)) {
            vehicleListDialog()
        }

        binding.exportReportIcon.setOnClickListener {
            val gsonArray = Gson().toJson(lastLocationsReport)
            exportReports(gsonArray)
        }
        binding.errorMessage.makeVisible()

        showSnackBar(binding.root, "Select vehicle from filters")
    }

    private fun initReport(locationList: List<LastLocationReport>) {
        if (locationList.isEmpty()) {
            showError()
            return
        }

        binding.progress.makeGone()
        binding.errorMessage.makeGone()
        binding.lastLocationReportRecyclerView.makeVisible()
        binding.lastLocationReportRecyclerView.apply {
            adapter = LastLocationReportAdapter(locationList)
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
    }

    private fun fetchLastLocations(imeiNumber: String, fromDate: String, toDate: String) {
        lifecycleScope.launchWhenResumed {
            val response = try {
                val locationsQuery =
                    LocationsQuery(Optional.Absent, Optional.Present(imeiNumber), fromDate, toDate)
                apolloClient.query(locationsQuery).execute()
            } catch (e: ApolloException) {
                showError()
                showToast(getString(R.string.error_message_default))
                Timber.e("Last Locations Exception : ${e.message}")
                return@launchWhenResumed
            }
            lastLocationsReport.clear()
            val locations = response.data?.locations ?: emptyList()
            locations.forEach { location ->
                location?.let {
                    val imei = location.IMEINumber ?: ""
                    val vehicleNum = location.vehicleNum ?: ""
                    val currentDate = location.currentDate ?: ""
                    val currentTime = location.currentTime ?: ""
                    val address = "Lat: ${location.latitude}, Long: ${location.longitude}"
                    val lat = location.latitude
                    val long = location.longitude
                    lastLocationsReport.add(LastLocationReport(imei, vehicleNum, currentDate, currentTime, address, lat, long))
                }
            }
            initReport(lastLocationsReport)
        }

    }

    private fun showError() {
        binding.progress.makeGone()
        binding.errorMessage.makeVisible()
        binding.lastLocationReportRecyclerView.makeGone()
    }


    private fun vehicleListDialog() {
        val vehicleList = ArrayList<String>()
        val vehicleMap = VehicleRepository.getVehicleNumberHashMap()
        val vehicleImeiList = ArrayList(vehicleMap.keys)
        vehicleMap.forEach {
            vehicleList.add(it.value)
        }
        context?.let {
            var dialog: AlertDialog? = null
            val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(it, android.R.layout.simple_list_item_1, vehicleList)
            val builder = AlertDialog.Builder(it)
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
                val selectedVehicleImei = vehicleImeiList[position]
                vehicleSelected(selectedVehicleImei, fromDate, toDate)
                dialog?.dismiss()
            }
            dialog = builder.create()
            dialog.show()
        }
    }


    private fun vehicleSelected(selectedVehicleImei: String, fromDate: String, toDate: String) {
        fetchLastLocations(selectedVehicleImei, fromDate, toDate)
        binding.progress.makeVisible()
        binding.errorMessage.makeGone()
        binding.lastLocationReportRecyclerView.makeGone()
    }


}