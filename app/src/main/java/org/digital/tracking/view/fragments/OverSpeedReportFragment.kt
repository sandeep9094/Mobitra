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
import org.digital.tracking.adapter.OverSpeedReportAdapter
import org.digital.tracking.databinding.FragmentOverspeedReportBinding
import org.digital.tracking.model.ApiResult
import org.digital.tracking.model.CustomDistanceReport
import org.digital.tracking.model.OverSpeedReport
import org.digital.tracking.model.csv.DistanceReportCsvModel
import org.digital.tracking.model.csv.OverSpeedReportCsvModel
import org.digital.tracking.repository.VehicleRepository
import org.digital.tracking.utils.*
import org.digital.tracking.viewModel.ReportsViewModel
import timber.log.Timber

@AndroidEntryPoint
class OverSpeedReportFragment : ReportsBaseFragment() {

    var overSpeedReport: ArrayList<OverSpeedReport> = ArrayList()
    private lateinit var binding: FragmentOverspeedReportBinding
    private val viewModel by activityViewModels<ReportsViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentOverspeedReportBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbarReports(binding.root, getString(R.string.title_over_speed_report)) {
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
                }
                is ApiResult.Error -> {
                    showError()
                }
                is ApiResult.Success -> {
                    val reportList = it.data
                    if (reportList.isEmpty()) {
                        showError()
                        return@observe
                    }
                    binding.progress.makeGone()
                    binding.error.makeGone()
                    binding.recyclerView.makeVisible()
                    initReport(reportList)
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
        overSpeedReport = ArrayList()
        list.forEach { report ->
            report?.overSpeedReport?.forEach { it ->
                overSpeedReport.add(
                    OverSpeedReport(
                        report.IMEINumber,
                        report.vehicleNum,
                        it?.currentDate,
                        it?.currentTime,
                        it?.latitude,
                        it?.longitude,
                        it?.speed
                    )
                )
            }
        }
        if (overSpeedReport.isEmpty()) {
            showError()
            return
        }
        binding.recyclerView.apply {
            adapter = OverSpeedReportAdapter(overSpeedReport)
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
            setHasFixedSize(true)
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
                Timber.d("VehicleFilter  Selected: ${vehicleList[position]}, from: $fromDate, to: $toDate")
                val selectedVehicleImei = vehicleImeiList[position]
                vehicleSelected(selectedVehicleImei, fromDate, toDate)
                dialog?.dismiss()
            }
            dialog = builder.create()
            dialog.show()
        }
    }

    private fun vehicleSelected(selectedVehicleImei: String, fromDate: String, toDate: String) {
        viewModel.getReport(selectedVehicleImei, fromDate, toDate)
    }


    private fun initExportReport() {
        binding.exportReportIcon.setOnClickListener {
            if (overSpeedReport.isEmpty()) {
                showToast(getString(R.string.error_message_reports_are_empty))
                return@setOnClickListener
            }
            viewLifecycleOwner.lifecycleScope.executeAsyncTask(onPreExecute = {
                showToast("Preparing Report to export")
            }, doInBackground = {
                val response = getExportList(overSpeedReport)
                return@executeAsyncTask response
            }, onPostExecute = {
                val gsonArray = Gson().toJson(it)
                val imeiNumber = overSpeedReport.first().imeiNumber ?: ""
                val reportName = "${getString(R.string.title_over_speed_report)}_${imeiNumber}".replace(" ", "_")
                exportReports(gsonArray, reportName)
            })
        }
    }

    private fun getExportList(reportList: ArrayList<OverSpeedReport>): List<OverSpeedReportCsvModel> {
        val list = ArrayList<OverSpeedReportCsvModel>()
        reportList.forEachIndexed { index, it ->
            val vehicleNumber = VehicleRepository.getVehicleNumber(it.imeiNumber ?: "")
            val dateTime = getReadableDateAndTime(it.date, it.time)
            val speed = "${it.speed?.getSpeedString()}"
            list.add(OverSpeedReportCsvModel(it.imeiNumber ?: "", vehicleNumber, dateTime, it.latitude, it.longitude, speed))
        }
        return list
    }

}