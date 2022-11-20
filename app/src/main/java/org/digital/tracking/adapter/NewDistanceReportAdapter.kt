package org.digital.tracking.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobitra.tracking.ReportsQuery
import org.digital.tracking.R
import org.digital.tracking.databinding.AdapterDistanceReportItemBinding
import org.digital.tracking.model.CustomDistanceReport
import org.digital.tracking.utils.getCompleteAddressString
import org.digital.tracking.utils.getReadableDateAndTime
import org.digital.tracking.utils.makeGone
import org.digital.tracking.utils.nA

class NewDistanceReportAdapter(
    private val distanceReportList: List<CustomDistanceReport>,
) : RecyclerView.Adapter<NewDistanceReportAdapter.ViewHolder>() {

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

        fun bindView(report: CustomDistanceReport?) {
            if (report == null) {
                binding.reportLayout.makeGone()
                return
            }
            if (report.startPoint != null) {
                //Show Start Point Info
                binding.startPointTextView.text = "Start Point"
                if (report.startPoint.currentDate.isNullOrEmpty()) {
                    binding.dateTimeTextView.text = nA
                } else {
                    binding.dateTimeTextView.text = getReadableDateAndTime(report.startPoint.currentDate, report.startPoint.currentTime)
                }
                binding.vehicleNumberTextView.text = report.vehicleNumber
                binding.addressTextView.text = binding.root.context.getCompleteAddressString(
                    report.startPoint.latitude,
                    report.startPoint.longitude
                )
                return
            }
            if (report.endPoint != null) {
                //Show End Point Info
                binding.startPointTextView.text = "End Point"
                if (report.endPoint.currentDate.isNullOrEmpty()) {
                    binding.dateTimeTextView.text = nA
                } else {
                    binding.dateTimeTextView.text = getReadableDateAndTime(report.endPoint.currentDate, report.endPoint.currentTime)
                }
                binding.vehicleNumberTextView.text = report.vehicleNumber
                binding.addressTextView.text = binding.root.context.getCompleteAddressString(
                    report.endPoint.latitude,
                    report.endPoint.longitude
                )

                return
            }

        }
    }

}