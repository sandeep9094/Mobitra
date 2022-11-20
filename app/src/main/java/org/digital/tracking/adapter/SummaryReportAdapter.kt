package org.digital.tracking.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobitra.tracking.LastLocationsQuery
import org.digital.tracking.R
import org.digital.tracking.databinding.AdapterOverSpeedReportItemBinding
import org.digital.tracking.databinding.AdapterSummaryReportItemBinding
import org.digital.tracking.model.OverSpeedReport
import org.digital.tracking.utils.*

class SummaryReportAdapter(
    private val overSpeedReportList: List<LastLocationsQuery.LastLocation?>
) : RecyclerView.Adapter<SummaryReportAdapter.ViewHolder>() {

    private lateinit var binding: AdapterSummaryReportItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_summary_report_item, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(overSpeedReportList[position])
    }

    override fun getItemCount(): Int = overSpeedReportList.size

    inner class ViewHolder(binding: AdapterSummaryReportItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(report: LastLocationsQuery.LastLocation?) {
            if (report == null) {
                binding.reportLayout.makeGone()
                return
            }
            //TODO add kms
            binding.vehicleNumberTextView.text = report.vehicleNum
            binding.distanceTextView.text = "${(0.0).getDistanceString()}"

        }
    }

}