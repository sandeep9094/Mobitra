package org.digital.tracking.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobitra.tracking.LastLocationsQuery
import com.mobitra.tracking.StopageReportQuery
import org.digital.tracking.R
import org.digital.tracking.databinding.AdapterOverSpeedReportItemBinding
import org.digital.tracking.databinding.AdapterStopageReportItemBinding
import org.digital.tracking.utils.*

class StopageReportAdapter(
    private val stopageReportList: List<StopageReportQuery.StoppageReport?>
) : RecyclerView.Adapter<StopageReportAdapter.ViewHolder>() {

    private lateinit var binding: AdapterStopageReportItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StopageReportAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_stopage_report_item, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StopageReportAdapter.ViewHolder, position: Int) {
        holder.bindView(stopageReportList[position])
    }

    override fun getItemCount(): Int = stopageReportList.size

    inner class ViewHolder(binding: AdapterStopageReportItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(report: StopageReportQuery.StoppageReport?) {
            if (report == null) {
                binding.reportLayout.makeGone()
                return
            }

            convertTimeStamp(binding.runningTime, report.totalRunningTime?.toLong())
            convertTimeStamp(binding.idealTime, report.totalIdealTime?.toLong())
            convertTimeStamp(binding.stopTime, report.totalStopTime?.toLong())

            binding.distanceTextView.text = "${(report.totalDistance?.toInt() ?: 0)}"
            binding.averageSpeedTextView.text = "${report.avgSpeed.formatDoubleValue()}"
            binding.maxSpeedTextView.text = "${report.maxSpeed.formatDoubleValue()}"
            binding.totalStopTextView.text = "${report.totalStops?.toInt() ?: 0}"
            binding.vehicleNumberTextView.text = report.vehicleNum
            binding.imeiNumber.text = report.IMEINumber
        }

        private fun convertTimeStamp(textView: TextView, durationInSeconds: Long?) {
            val durationInMillis: Long = (durationInSeconds ?: 0L) * 1000L
            if (durationInMillis == 0L) {
                textView.text = "00:00:00"
                return
            }
            val second: Long = durationInMillis / 1000 % 60
            val minute: Long = durationInMillis / (1000 * 60) % 60
            val hour: Long = durationInMillis / (1000 * 60 * 60) % 24

            var timeString = String.format("%02dh %02dm %02ds", hour, minute, second)
            if (hour < 10) {
                timeString = String.format("%02dh %02dm %02ds", hour, minute, second)
            }
            textView.text = timeString
        }

    }

}