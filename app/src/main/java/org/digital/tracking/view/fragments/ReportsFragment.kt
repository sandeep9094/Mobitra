package org.digital.tracking.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.digital.tracking.R
import org.digital.tracking.adapter.MenuAdapter
import org.digital.tracking.databinding.FragmentReportsBinding
import org.digital.tracking.model.MenuModel
import org.digital.tracking.repository.MenuRepository
import org.digital.tracking.utils.navigateToFragment
import org.digital.tracking.utils.showToast
import org.digital.tracking.viewModel.ReportsViewModel

@AndroidEntryPoint
class ReportsFragment : BaseFragment(), MenuAdapter.Listener {

    private lateinit var binding: FragmentReportsBinding
    private val viewModel by activityViewModels<ReportsViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentReportsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHomeToolbar(binding.root)

        initReportsMenuList()
    }

    private fun initReportsMenuList() {
        val menuList = MenuRepository.getReportsMenuList()
        binding.reportsMenuRecyclerView.setHasFixedSize(true)
        binding.reportsMenuRecyclerView.adapter = MenuAdapter(menuList, this)
        binding.reportsMenuRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.reportsMenuRecyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
    }

    override fun onMenuItemClicked(menuModel: MenuModel) {
        when (menuModel.titleResId) {
            R.string.title_distance_report -> {
                navigateToFragment(R.id.action_navigation_reports_to_distanceReportFragment)
            }
            R.string.title_daily_distance_report -> {
                navigateToFragment(R.id.action_navigation_reports_to_dailyDistanceReportFragment)
            }
            R.string.title_over_speed_report -> {
                navigateToFragment(R.id.action_navigation_reports_to_overSpeedReportFragment)
            }
            R.string.title_last_location_report -> {
                navigateToFragment(R.id.action_navigation_reports_to_lastLocationFragment)
            }
            R.string.title_fuel_consumption_report -> {
                navigateToFragment(R.id.action_navigation_reports_to_fuelConsumptionReportFragment)
            }
            R.string.title_halt_report -> {
                navigateToFragment(R.id.action_navigation_reports_to_haltReportFragment)
            }
            R.string.title_summary_report -> {
                navigateToFragment(R.id.action_navigation_reports_to_summaryReportFragment)
            }
            R.string.title_stopage_summary -> {
                navigateToFragment(R.id.action_navigation_reports_to_stopageReportFragment)
            }
            else -> context.showToast(menuModel.titleResId)
        }
    }
}