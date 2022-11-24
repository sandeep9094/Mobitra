package org.digital.tracking.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobitra.tracking.LastLocationsQuery
import com.mobitra.tracking.ReportsQuery
import org.digital.tracking.R
import org.digital.tracking.databinding.AdapterDailyDistanceReportItemBinding
import org.digital.tracking.databinding.AdapterDistanceReportItemBinding
import org.digital.tracking.model.DailyReport
import org.digital.tracking.model.DistanceReport
import org.digital.tracking.utils.*

class DistanceReportAdapter(
    private val distanceReportList: List<DailyReport>
) : RecyclerView.Adapter<DistanceReportAdapter.ViewHolder>() {

    private lateinit var binding: AdapterDailyDistanceReportItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_daily_distance_report_item, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(distanceReportList[position])
    }

    override fun getItemCount(): Int = distanceReportList.size

    inner class ViewHolder(private val binding: AdapterDailyDistanceReportItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(report: DailyReport) {
            if (report.startDateTime.isEmpty()) {
                binding.dateTimeTextView.text = nA
            } else {
                binding.dateTimeTextView.text = report.startDateTime.getReadableDateWithTime()
            }
            binding.distanceTextView.text = "${report.totalDistance.getDistanceString()}"
        }
    }

}