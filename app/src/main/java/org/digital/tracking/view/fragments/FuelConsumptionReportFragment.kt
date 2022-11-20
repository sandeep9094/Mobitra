package org.digital.tracking.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import org.digital.tracking.R
import org.digital.tracking.databinding.FragmentFuelConsumptionReportBinding
import org.digital.tracking.model.FuelConsumptionReport

class FuelConsumptionReportFragment : BaseFragment() {

    private lateinit var binding: FragmentFuelConsumptionReportBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_fuel_consumption_report, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.root, R.string.title_fuel_consumption_report)

        val dummyFuelConsumptionReport = FuelConsumptionReport("HR47D6782", "15440.3", "3189.5")
        binding.fuelConsumptionReport = dummyFuelConsumptionReport
        binding.lifecycleOwner = viewLifecycleOwner
    }

}