package org.digital.tracking.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobitra.tracking.LastLocationsQuery
import com.mobitra.tracking.ReportsQuery
import org.digital.tracking.R
import org.digital.tracking.databinding.AdapterDistanceReportItemBinding
import org.digital.tracking.model.DistanceReport
import org.digital.tracking.utils.*

class DistanceReportAdapter(
    private val distanceReportList: List<ReportsQuery.Report?>,
    private val fromDailyDistance: Boolean
) : RecyclerView.Adapter<DistanceReportAdapter.ViewHolder>() {

    private lateinit var binding: AdapterDistanceReportItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_distance_report_item, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(distanceReportList[position])
    }

    override fun getItemCount(): Int = distanceReportList.size

    inner class ViewHolder(private val binding: AdapterDistanceReportItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(report: ReportsQuery.Report?) {
            if (report == null) {
                binding.reportLayout.makeGone()
                return
            }
            if (report.startPoint?.currentDate.isNullOrEmpty()) {
                binding.dateTimeTextView.text = nA
            } else {
                binding.dateTimeTextView.text = getReadableDateAndTime(report.startPoint?.currentDate, report.startPoint?.currentTime)
            }
            binding.vehicleNumberTextView.text = report.vehicleNum
            binding.addressTextView.text =
                binding.root.context.getCompleteAddressString(report.startPoint?.latitude, report.startPoint?.longitude)
            if (fromDailyDistance) {
                binding.dateTimeTextView.makeGone()
            } else {

            }
        }
    }

}