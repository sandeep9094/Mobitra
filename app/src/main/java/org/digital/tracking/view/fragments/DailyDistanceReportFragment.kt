package org.digital.tracking.view.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
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
import com.mobitra.tracking.LastLocationsQuery
import com.mobitra.tracking.ReportsQuery
import dagger.hilt.android.AndroidEntryPoint
import org.digital.tracking.R
import org.digital.tracking.adapter.DistanceReportAdapter
import org.digital.tracking.databinding.FragmentDailyDistanceReportBinding
import org.digital.tracking.model.ApiResult
import org.digital.tracking.model.CustomDistanceReport
import org.digital.tracking.model.DailyReport
import org.digital.tracking.repository.VehicleRepository
import org.digital.tracking.utils.*
import org.digital.tracking.viewModel.ReportsViewModel
import timber.log.Timber

@AndroidEntryPoint
class DailyDistanceReportFragment : ReportsBaseFragment() {

    private var dailyReportList: List<DailyReport> = ArrayList<DailyReport>()
    private val viewModel by activityViewModels<ReportsViewModel>()
    private lateinit var binding: FragmentDailyDistanceReportBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDailyDistanceReportBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbarReports(binding.root, getString(R.string.title_daily_distance_report)) {
            vehicleListDialog()
        }

        initExportReport()
        setupObserver()
        showSnackBar(binding.root, "Select vehicle from filters")
    }

    private fun setupObserver() {
        viewModel.dailyDistanceReportResult.observe(viewLifecycleOwner) {
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
                    Log.d("Reports", "Daily Distance observer: ${reportList}")
                    if (reportList.isEmpty()) {
                        showError()
                        return@observe
                    }
                    Log.d("Reports", "Daily Distance observer: ${reportList}")
                    binding.progress.makeGone()
                    binding.error.makeGone()
                    binding.recyclerView.makeVisible()
                    dailyReportList = reportList
                    initReport(dailyReportList)
                }
            }
        }
    }

    private fun showError() {
        binding.progress.makeGone()
        binding.error.makeVisible()
        binding.recyclerView.makeGone()
    }

    private fun initReport(list: List<DailyReport>) {
        binding.recyclerView.apply {
            adapter = DistanceReportAdapter(list)
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
                vehicleSelected(selectedVehicleImei, fromDate, toDate)
                dialog?.dismiss()
            }
            dialog = builder.create()
            dialog.show()
        }
    }


    private fun vehicleSelected(selectedVehicleImei: String, fromDate: String, toDate: String) {
        viewModel.getDailyDistanceReport(selectedVehicleImei, fromDate, toDate)

    }


    private fun initExportReport() {
        binding.exportReportIcon.setOnClickListener {
//            if (dailyReportList.isEmpty()) {
//                showToast(getString(R.string.error_message_reports_are_empty))
//                return@setOnClickListener
//            }
//            viewLifecycleOwner.lifecycleScope.executeAsyncTask(onPreExecute = {
//                showToast("Preparing Report to export")
//            }, doInBackground = {
//                val response = getExportList(dailyReportList)
//                return@executeAsyncTask response
//            }, onPostExecute = {
//                val gsonArray = Gson().toJson(it)
//                val imeiNumber = dailyReportList.first().imeiNumber ?: ""
//                val reportName = "${getString(R.string.title_distance_report)}_${imeiNumber}".replace(" ", "_")
//                exportReports(gsonArray, reportName)
//            })
        }
    }


}