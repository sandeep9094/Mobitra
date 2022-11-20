package org.digital.tracking.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobitra.tracking.LastLocationsQuery
import com.mobitra.tracking.ReportsQuery
import dagger.hilt.android.AndroidEntryPoint
import org.digital.tracking.R
import org.digital.tracking.adapter.OverSpeedReportAdapter
import org.digital.tracking.adapter.SummaryReportAdapter
import org.digital.tracking.databinding.FragmentSummaryReportBinding
import org.digital.tracking.model.ApiResult
import org.digital.tracking.repository.VehicleRepository
import org.digital.tracking.utils.makeGone
import org.digital.tracking.utils.makeVisible
import org.digital.tracking.viewModel.ReportsViewModel

@AndroidEntryPoint
class SummaryReportFragment : BaseFragment() {

    private lateinit var binding: FragmentSummaryReportBinding
    private val viewModel by activityViewModels<ReportsViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSummaryReportBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbarReports(binding.root, getString(R.string.title_summary_report)) {
//            vehicleListDialog()
        }


//        viewModel.getReport()

        setupObserver()
    }

    private fun setupObserver() {
        //TODO update reportApiResult
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
//        binding.recyclerView.apply {
//            adapter = SummaryReportAdapter(list)
//            layoutManager = LinearLayoutManager(context)
//            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
//        }
    }

//    private fun vehicleListDialog() {
//        val vehicleList: ArrayList<String> = ArrayList()
//        VehicleRepository.getVehicleNumberList().forEach { vehicleNum ->
//            vehicleNum.let {
//                vehicleList.add(it)
//            }
//        }
//        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, vehicleList)
//        val builder = AlertDialog.Builder(this)
//        builder.setTitle("Select Vehicle")
//
//        builder.setSingleChoiceItems(arrayAdapter, -1) { dialog, which ->
//            fetchVehicleLocations(vehicleList[which])
//            dialog.dismiss()
//        }
//        builder.create().show()
//    }


}