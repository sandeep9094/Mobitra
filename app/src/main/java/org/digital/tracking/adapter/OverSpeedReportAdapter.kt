package org.digital.tracking.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobitra.tracking.LastLocationsQuery
import com.mobitra.tracking.ReportsQuery
import org.digital.tracking.R
import org.digital.tracking.databinding.AdapterOverSpeedReportItemBinding
import org.digital.tracking.model.OverSpeedReport
import org.digital.tracking.repository.VehicleRepository
import org.digital.tracking.utils.*

class OverSpeedReportAdapter(
    private val overSpeedReportList: List<OverSpeedReport>
) : RecyclerView.Adapter<OverSpeedReportAdapter.ViewHolder>() {

    private lateinit var binding: AdapterOverSpeedReportItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_over_speed_report_item, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(overSpeedReportList[position])
    }

    override fun getItemCount(): Int = overSpeedReportList.size

    inner class ViewHolder(binding: AdapterOverSpeedReportItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(report: OverSpeedReport?) {
            if (report == null) {
                binding.reportLayout.makeGone()
                return
            }
            binding.vehicleNumberTextView.text = VehicleRepository.getVehicleNumber(report.imeiNumber ?: "")
            binding.speedTextView.text = "${report.speed?.getSpeedString()}"
            binding.dateTimeTextView.text = getReadableDateAndTime(report.date, report.time)
            binding.addressTextView.text = binding.root.context.getCompleteAddressString(report.latitude, report.longitude)
        }
    }

}