package org.digital.tracking.repository

import com.mobitra.tracking.LastLocationsQuery
import org.digital.tracking.bindingAdapter.getVehicleStatus
import timber.log.Timber

object VehicleRepository {

    private var vehicleNumberHashMap: LinkedHashMap<String, String> = LinkedHashMap()
    private var vehicleNumberList: ArrayList<String> = ArrayList()
    private var vehiclesMapWithStatus: Map<String, List<LastLocationsQuery.LastLocation?>> = HashMap()
    private var vehicleList: List<LastLocationsQuery.LastLocation?> = ArrayList()

    fun setVehicleList(list: List<LastLocationsQuery.LastLocation?>) {
        val nonNullList = list.filterNotNull()
        vehicleList = nonNullList.distinctBy { it.IMEINumber }.filterNot { it.vehicleNum.isNullOrEmpty() }
        vehicleNumberList.clear() //Clear older Vehicle list
        vehicleList.forEach { location ->
            val imeiNumber = location?.IMEINumber ?: ""
            val vehicleNumber = location?.vehicleNum ?: ""
            if (imeiNumber.isNotEmpty() && vehicleNumber.isNotEmpty()) {
                vehicleNumberList.add(vehicleNumber)
                vehicleNumberHashMap[imeiNumber] = vehicleNumber
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

    fun getVehicleNumberList(): ArrayList<String> {
        return vehicleNumberList
    }

    fun getVehicleImei(vehicleNumber: String): String {
        val vehicle = vehicleList.find { it?.vehicleNum == vehicleNumber }
        return vehicle?.IMEINumber ?: ""
    }

    fun getDistance(): List<Double> {
        val distanceList = ArrayList<Double>()

        distanceList.add(528.20)
        distanceList.add(115.22)
        distanceList.add(996.64)
        distanceList.add(740.25)
        distanceList.add(421.78)
        distanceList.add(904.04)
        distanceList.add(578.03)
        distanceList.add(867.15)
        distanceList.add(509.47)
        distanceList.add(983.82)
        distanceList.add(563.58)
        distanceList.add(600.68)

        return distanceList
    }

}