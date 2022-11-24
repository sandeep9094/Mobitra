package org.digital.tracking.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobitra.tracking.LastLocationsQuery
import org.digital.tracking.R
import org.digital.tracking.databinding.AdapterHaltReportItemBinding
import org.digital.tracking.model.HaltReport
import org.digital.tracking.utils.*

class HaltReportAdapter(
    private val haltReportList: List<HaltReport?>
) : RecyclerView.Adapter<HaltReportAdapter.ViewHolder>() {

    private lateinit var binding: AdapterHaltReportItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_halt_report_item, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(haltReportList[position])
    }

    override fun getItemCount(): Int = haltReportList.size

    inner class ViewHolder(private val binding: AdapterHaltReportItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(haltReport: HaltReport?) {
            if (haltReport == null) {
                binding.reportLayout.makeGone()
                return
            }
            binding.dateTimeTextView.text = getReadableDateAndTime(haltReport.date, haltReport.time)
            binding.vehicleNumberTextView.text = haltReport.vehicleNumber
            binding.haltsCountTextView.text = "${haltReport.halts ?: 0}"
            binding.addressTextView.text = binding.root.context.getCompleteAddressString(haltReport.latitude, haltReport.longitude)
        }
    }

}