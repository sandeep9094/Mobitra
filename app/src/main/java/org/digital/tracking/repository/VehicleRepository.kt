package org.digital.tracking.repository

import com.mobitra.tracking.LastLocationsQuery
import org.digital.tracking.bindingAdapter.getVehicleStatus
import org.digital.tracking.repository.cache.UserCacheManager
import timber.log.Timber

object VehicleRepository {

    private var vehicleNumberHashMap: LinkedHashMap<String, String> = LinkedHashMap()
    private var vehiclesMapWithStatus: Map<String, List<LastLocationsQuery.LastLocation?>> = HashMap()
    private var vehicleList: List<LastLocationsQuery.LastLocation?> = ArrayList()

    fun destroy() {
        vehicleNumberHashMap.clear()
        vehicleList = ArrayList()
        vehiclesMapWithStatus = HashMap()
    }

    fun setVehicleList(list: List<LastLocationsQuery.LastLocation?>) {
        val nonNullList = list.filterNotNull()
        vehicleList = nonNullList.distinctBy { it.IMEINumber }.filterNot { it.vehicleNum.isNullOrEmpty() }
        vehicleList.forEach { location ->
            val imeiNumber = location?.IMEINumber ?: ""
            val vehicleNumber = location?.vehicleNum ?: ""
            if (imeiNumber.isNotEmpty() && vehicleNumber.isNotEmpty()) {
                vehicleNumberHashMap[imeiNumber] = getVehicleNumber(imeiNumber)
            }
        }
        initVehicleStatusCount()
    }

    private fun initVehicleStatusCount() {
        vehiclesMapWithStatus = vehicleList.groupBy { it.getVehicleStatus() }
        Timber.d("Vehicle Status Count : $vehiclesMapWithStatus")
    }

    fun getVehicleStatusMap(): Map<String, List<LastLocationsQuery.LastLocation?>> {
        return vehiclesMapWithStatus
    }

    fun getVehicleNumberHashMap(): LinkedHashMap<String, String> {
        return vehicleNumberHashMap
    }

    fun getVehicleList(): List<LastLocationsQuery.LastLocation?> {
        return vehicleList
    }

    fun getVehicleNumber(imeiNumber: String): String {
        val deviceVehicleNumber: String? = UserCacheManager.getDeviceFromImei(imeiNumber)?.vehicleNumber
        return deviceVehicleNumber ?: "N/A"
    }

}