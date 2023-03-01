package org.digital.tracking.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.mobitra.tracking.ReportsQuery
import dagger.hilt.android.AndroidEntryPoint
import org.digital.tracking.R
import org.digital.tracking.adapter.NewDistanceReportAdapter
import org.digital.tracking.databinding.FragmentDistanceReportBinding
import org.digital.tracking.model.ApiResult
import org.digital.tracking.model.CustomDistanceReport
import org.digital.tracking.model.LastLocationReport
import org.digital.tracking.model.csv.DistanceReportCsvModel
import org.digital.tracking.model.csv.LastLocationCsvModel
import org.digital.tracking.repository.VehicleRepository
import org.digital.tracking.utils.*
import org.digital.tracking.viewModel.ReportsViewModel
import timber.log.Timber

@AndroidEntryPoint
class DistanceReportFragment : ReportsBaseFragment() {

    private val customDistanceReport = ArrayList<CustomDistanceReport>()
    private lateinit var binding: FragmentDistanceReportBinding
    private val viewModel by activityViewModels<ReportsViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDistanceReportBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbarReports(binding.root, getString(R.string.title_distance_report)) {
            vehicleListDialog()
        }

        initExportReport()
        setupObserver()
        showSnackBar(binding.root, "Select vehicle from filters")
    }

    private fun setupObserver() {
        viewModel.reportApiResult.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResult.Loading -> {
                    binding.progress.makeVisible()
                    binding.error.makeGone()
                    binding.recyclerView.makeGone()
                    binding.totalDistanceLayout.makeGone()
                }
                is ApiResult.Error -> {
                    showError()
                }
                is ApiResult.Success -> {
                    val haltReportList = it.data
                    if (haltReportList.isEmpty()) {
                        showError()
                        return@observe
                    }
                    binding.progress.makeGone()
                    binding.error.makeGone()
                    binding.recyclerView.makeVisible()
                    initReport(haltReportList)
                }
            }
        }
        viewModel.totalDistanceReportApiResult.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResult.Loading -> {
                }
                is ApiResult.Error -> {
                }
                is ApiResult.Success -> {
                    val totalDistanceReport = it.data
                    if (totalDistanceReport.isEmpty()) {
                        return@observe
                    }
                    try {
                        val distanceReport = totalDistanceReport.first()
                        binding.totalDistanceTextView.text = distanceReport?.totalDistance.getDistanceString()
                        binding.totalDistanceLayout.makeVisible()
                    } catch (exception: Exception) {
                        binding.totalDistanceLayout.makeGone()
                    }
                }
            }
        }
    }

    private fun showError() {
        binding.progress.makeGone()
        binding.error.makeVisible()
        binding.recyclerView.makeGone()
    }

    private fun initReport(list: List<ReportsQuery.Report?>) {
        customDistanceReport.clear()
        list.forEach {
            customDistanceReport.add(CustomDistanceReport(it?.IMEINumber, it?.vehicleNum, true, it?.startPoint, null))
            customDistanceReport.add(CustomDistanceReport(it?.IMEINumber, it?.vehicleNum, false, null, it?.endPoint))
        }
        binding.recyclerView.apply {
            adapter = NewDistanceReportAdapter(customDistanceReport)
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
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
                Timber.d("VehicleFilter  Selected: $selectedVehicleImei, from: $fromDate, to: $toDate")
                vehicleSelected(selectedVehicleImei, fromDate, toDate)
                dialog?.dismiss()
            }
            dialog = builder.create()
            dialog.show()
        }
    }


    private fun vehicleSelected(selectedVehicleImei: String, fromDate: String, toDate: String) {
        viewModel.getReport(selectedVehicleImei, fromDate, toDate)
        viewModel.getTotalDistanceReport(selectedVehicleImei, fromDate, toDate)
    }

    private fun initExportReport() {
        binding.exportReportIcon.setOnClickListener {
            if (customDistanceReport.isEmpty()) {
                showToast(getString(R.string.error_message_reports_are_empty))
                return@setOnClickListener
            }
            viewLifecycleOwner.lifecycleScope.executeAsyncTask(onPreExecute = {
                showToast("Preparing Report to export")
            }, doInBackground = {
                val response = getExportList(customDistanceReport)
                return@executeAsyncTask response
            }, onPostExecute = {
                val gsonArray = Gson().toJson(it)
                val imeiNumber = customDistanceReport.first().imeiNumber ?: ""
                val reportName = "${getString(R.string.title_distance_report)}_${imeiNumber}".replace(" ", "_")
                exportReports(gsonArray, reportName)
            })
        }
    }

    private fun getExportList(reportList: ArrayList<CustomDistanceReport>): List<DistanceReportCsvModel> {
        val list = ArrayList<DistanceReportCsvModel>()
        reportList.forEachIndexed { index, it ->
            Timber.d("Report: Current index : $index")
            var point = ""
            var dateTime = ""
            var address = ""
            var latitude: Double? = null
            var longitude: Double? = null
            val vehicleNumber = VehicleRepository.getVehicleNumber(it.imeiNumber ?: "")
            if (it.startPoint != null) {
                //Show Start Point Info
                point = "Start Point"
                dateTime = if (it.startPoint.currentDate.isNullOrEmpty()) {
                    ""
                } else {
                    getReadableDateAndTime(it.startPoint.currentDate, it.startPoint.currentTime)
                }
                latitude = it.startPoint.latitude
                longitude = it.startPoint.longitude
                address = binding.root.context.getCompleteAddressString(
                    it.startPoint.latitude,
                    it.startPoint.longitude
                )
            } else if (it.endPoint != null) {
                //Show End Point Info
                point = "End Point"
                dateTime = if (it.endPoint.currentDate.isNullOrEmpty()) {
                    ""
                } else {
                    getReadableDateAndTime(it.endPoint.currentDate, it.endPoint.currentTime)
                }
                latitude = it.endPoint.latitude
                longitude = it.endPoint.longitude
                address = binding.root.context.getCompleteAddressString(
                    it.endPoint.latitude,
                    it.endPoint.longitude
                )
            }
            list.add(DistanceReportCsvModel(point, vehicleNumber, dateTime, address, latitude, longitude))
        }
        return list
    }

}