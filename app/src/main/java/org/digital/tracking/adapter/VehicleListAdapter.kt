package org.digital.tracking.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.exception.ApolloException
import com.mobitra.tracking.LastLocationsQuery
import com.mobitra.tracking.StopageReportQuery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.digital.tracking.R
import org.digital.tracking.bindingAdapter.*
import org.digital.tracking.databinding.AdapterVehicleItemBinding
import org.digital.tracking.databinding.AdapterVehicleItemWithIconsBinding
import org.digital.tracking.maps.MapUtils
import org.digital.tracking.model.ApiResult
import org.digital.tracking.model.VehicleModel
import org.digital.tracking.repository.cache.UserCacheManager
import org.digital.tracking.utils.*

class VehicleListAdapter(
    private val vehicleList: List<LastLocationsQuery.LastLocation?>,
    private val listener: Listener,
    private val apolloClient: ApolloClient
) :
    RecyclerView.Adapter<VehicleListAdapter.ViewHolder>() {

    private lateinit var binding: AdapterVehicleItemWithIconsBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(inflater, R.layout.adapter_vehicle_item_with_icons, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(vehicleList[position])
    }

    override fun getItemCount(): Int = vehicleList.size

    inner class ViewHolder(binding: AdapterVehicleItemWithIconsBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(vehicle: LastLocationsQuery.LastLocation?) {
            if (vehicle == null) {
                binding.root.makeGone()
            }
            binding.imageView.makeVisible()
            val deviceVehicleNumber: String? = UserCacheManager.getDeviceFromImei(vehicle?.IMEINumber ?: "")?.vehicleNumber
            val vehicleNumber: String = deviceVehicleNumber ?: (vehicle?.vehicleNum ?: "")
            binding.vehicleNumber.text = vehicleNumber
            binding.addressTextView.text = binding.addressTextView.context.getCompleteAddressString(vehicle?.latitude, vehicle?.longitude)
            binding.lastContact.text = getReadableDateAndTime(vehicle?.currentDate, vehicle?.currentTime)
            binding.speedTextView.text = vehicle?.speed.getSpeedString()
            binding.movedKmTextView.text = vehicle?.totalDistance.getDistanceString()
            //Ignition Status
            if (vehicle?.ignitionStat?.toInt() == Constants.IGNITION_STAT_ON) {
                binding.ignitionStatus.setIgnitionStatusIconColor(isIgnitionOn = true)
            } else {
                binding.ignitionStatus.setIgnitionStatusIconColor(isIgnitionOn = false)
            }

            val vehicleStatus = vehicle.getVehicleStatus()
            binding.vehicleNumber.setVehicleNumberColor(vehicleStatus)
            binding.vehicleStatusText.setVehicleStatusText(vehicleStatus)
            binding.vehicleStatusIcon.setVehicleStatusIconColor(vehicleStatus)
            binding.setVehicleIconStatus.setVehicleIconStatus(vehicleStatus)
            binding.signalIcon.setVehicleSignalColor(vehicle?.gsmSigStr)
            binding.gpsIcon.setGpsIconColor(vehicle?.gpsFixState)

            binding.vehicleRootLayout.setOnClickListener {
                if (vehicleStatus.contains(Constants.VEHICLE_STATUS_KEY_STOP, ignoreCase = true)) {
                    listener.onVehicleItemClick(vehicle, vehicleStatus, binding.vehicleStatusText.text.toString())
                } else {
                    listener.onVehicleItemClick(vehicle, vehicleStatus)
                }
            }
            binding.replayIcon.setOnClickListener {
                listener.onVehicleReplayClick(vehicle)
            }
            binding.shareIcon.setOnClickListener {
                listener.onVehicleShareLocationClick(vehicle)
            }
//            if (vehicleStatus.contains(Constants.VEHICLE_STATUS_KEY_STOP, ignoreCase = true)) {
//                getStopageReports(vehicleStatus, vehicle?.IMEINumber ?: "")
//            }
        }

        fun getStopageReports(
            vehicleStatus: String,
            imeiNumber: String,
            fromDate: String = getMonthStartDate(),
            toDate: String = getMonthEndDate()
        ) {
            CoroutineScope(Dispatchers.IO).launch {
                val response = try {
                    val query = StopageReportQuery(imeiNumber, fromDate, toDate)
                    apolloClient.query(query).execute()
                } catch (e: ApolloException) {
                    return@launch
                }
                val reports = response.data?.stoppageReport
                if (reports.isNullOrEmpty()) {
                    return@launch
                }
                val stoppageReport = reports.first()
                convertTimeStamp(vehicleStatus, binding.vehicleStatusText, stoppageReport?.totalStopTime?.toLong())
            }
        }

        private fun convertTimeStamp(vehicleStatus: String, textView: TextView, duration: Long?) {
            val durationInMillis = (duration ?: 0L) * 1000L
            if (durationInMillis == 0L) {
                textView.text = "Stop(00:00:00)"
                return
            }
            val second: Long = durationInMillis / 1000 % 60
            val minute: Long = durationInMillis / (1000 * 60) % 60
            val hour: Long = durationInMillis / (1000 * 60 * 60) % 24

            var timeString = String.format("%02d:%02d:%02d", hour, minute, second)
            if (hour < 10) {
                timeString = String.format("%02d:%02d:%02d", hour, minute, second)
            }
            textView.text = "Stop($timeString)"
        }

    }

    interface Listener {
        fun onVehicleReplayClick(vehicle: LastLocationsQuery.LastLocation?)
        fun onVehicleShareLocationClick(vehicle: LastLocationsQuery.LastLocation?)
        fun onVehicleItemClick(vehicle: LastLocationsQuery.LastLocation?, vehicleStatus: String = "", statusTextViewString: String = "")
    }

}