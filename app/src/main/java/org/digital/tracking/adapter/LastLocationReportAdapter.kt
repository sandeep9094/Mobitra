package org.digital.tracking.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.digital.tracking.R
import org.digital.tracking.databinding.AdapterLastLocationReportItemBinding
import org.digital.tracking.model.LastLocationReport
import org.digital.tracking.repository.VehicleRepository
import org.digital.tracking.utils.getCompleteAddressString
import org.digital.tracking.utils.getReadableDateAndTime
import org.digital.tracking.utils.getReadableDateWithTime

class LastLocationReportAdapter(
    private val lastLocationReportList: List<LastLocationReport>
) : RecyclerView.Adapter<LastLocationReportAdapter.ViewHolder>() {

    private lateinit var binding: AdapterLastLocationReportItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_last_location_report_item, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(lastLocationReportList[position])
    }

    override fun getItemCount(): Int = lastLocationReportList.size

    inner class ViewHolder(private val binding: AdapterLastLocationReportItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(lastLocationReport: LastLocationReport) {
            binding.lastLocationReport = lastLocationReport
            binding.executePendingBindings()
            binding.vehicleNumberTextView.text = VehicleRepository.getVehicleNumber(lastLocationReport.imeiNumber)
            binding.dateTimeTextView.text = getReadableDateAndTime(lastLocationReport.date, lastLocationReport.time)
            val address = binding.root.context.getCompleteAddressString(lastLocationReport.lat, lastLocationReport.long)
            binding.addressTextView.text = address
        }

    }

}
