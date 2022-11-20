package org.digital.tracking.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.digital.tracking.R
import org.digital.tracking.adapter.StopResumeVehicleAdapter
import org.digital.tracking.databinding.FragmentStopResumeVehicleBinding
import org.digital.tracking.repository.VehicleRepository

class StopResumeVehicleFragment : BaseFragment() {

    private lateinit var binding: FragmentStopResumeVehicleBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentStopResumeVehicleBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.root, getString(R.string.title_stop_resume_vehicle))
        initVehicleRecyclerView()
    }

    fun initVehicleRecyclerView() {
//        val vehicleList = VehicleRepository.vehicleList
//        binding.recyclerView.apply {
//            adapter = StopResumeVehicleAdapter(vehicleList)
//            layoutManager = LinearLayoutManager(context)
//            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
//        }
    }

}