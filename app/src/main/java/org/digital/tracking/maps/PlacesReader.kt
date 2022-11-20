package org.digital.tracking.maps

import org.digital.tracking.repository.VehicleRepository
import timber.log.Timber

class PlacesReader {

    fun read(): List<Place> {
        val mapsPlaces = ArrayList<Place>()
        val vehicles = VehicleRepository.getVehicleList()
        vehicles.forEach { vehicle ->
            if (vehicle?.latitude != null && vehicle.longitude != null) {
                mapsPlaces.add(Place(vehicle.vehicleNum ?: "Unknown", vehicle.IMEINumber ?: "", vehicle.latitude, vehicle.longitude))
            }
        }
        Timber.d("PlacesReader: mapsPlaces : ${mapsPlaces}")
//        VehicleRepository.getVehicleNumberList().forEach {  toGetVehicleNumber ->
//            val vehicle = vehicles.find { it?.vehicleNum == toGetVehicleNumber}
//            if (vehicle?.latitude != null && vehicle.longitude != null) {
//                mapsPlaces.add(Place(vehicle.vehicleNum ?: "Unknown", vehicle.latitude, vehicle.longitude))
//            }
//        }
        return mapsPlaces
    }
}