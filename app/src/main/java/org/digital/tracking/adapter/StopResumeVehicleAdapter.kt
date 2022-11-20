package org.digital.tracking.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.digital.tracking.R
import org.digital.tracking.databinding.AdapterStopResumeVehicleItemBinding
import org.digital.tracking.model.VehicleModel

class StopResumeVehicleAdapter(private val vehicleList: List<VehicleModel>) : RecyclerView.Adapter<StopResumeVehicleAdapter.ViewHolder>() {

    private lateinit var binding: AdapterStopResumeVehicleItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.adapter_stop_resume_vehicle_item, parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(vehicleList[position])
    }

    override fun getItemCount(): Int = vehicleList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(vehicle: VehicleModel) {
            binding.vehicle = vehicle
            binding.executePendingBindings()
        }

    }

}