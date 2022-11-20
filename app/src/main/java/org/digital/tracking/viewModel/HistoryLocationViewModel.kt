package org.digital.tracking.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloException
import com.mobitra.tracking.LastLocationsQuery
import com.mobitra.tracking.LocationsQuery
import com.mobitra.tracking.ReceiveLocationSubscription
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.digital.tracking.di.ResourceProvider
import org.digital.tracking.enum.ReplayViewSpeed
import org.digital.tracking.model.ApiResult
import org.digital.tracking.repository.MapsDummyRepository
import org.digital.tracking.repository.VehicleRepository
import org.digital.tracking.utils.getTodayDate
import org.digital.tracking.utils.getYesterdayDate
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HistoryLocationViewModel @Inject constructor(
    private val apolloClient: ApolloClient,
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    private var pushLocationJob: Job? = null
    var replayViewSpeed: Long = ReplayViewSpeed.NORMAL_3X
    val replayLocations: MutableLiveData<List<LocationsQuery.Location?>> = MutableLiveData()
    val vehicleNameListResult: MutableLiveData<ArrayList<String>> = MutableLiveData(ArrayList())

    // In history/ replay current point of vehicle at coordinates
    val replayLocationCurrentIndex: MutableLiveData<Int> = MutableLiveData(0)
    val vehicleHistoryMovingLocation: MutableLiveData<LocationsQuery.Location?> = MutableLiveData()

    //    val historyLocationsResult: MutableLiveData<ApiResult<List<LocationsQuery.Location?>>> = MutableLiveData()
    val historyLocationsResult: MutableLiveData<ApiResult<List<LocationsQuery.Location?>>> = MutableLiveData()

    fun getLocations(imeiNumber: String, fromDate: String, toDate: String) {
        getHistoryLocations(imeiNumber, fromDate, toDate)
    }

    private fun getHistoryLocations(imeiNumber: String, fromDate: String, toDate: String) {
        viewModelScope.launch {
            val response = try {
                val locationsQuery =
                    LocationsQuery(Optional.Absent, Optional.Present(imeiNumber), fromDate, toDate)
                apolloClient.query(locationsQuery).execute()
            } catch (e: ApolloException) {
                historyLocationsResult.value = ApiResult.Error(resourceProvider.defaultError)
                return@launch
            }
            val lastLocations = response.data?.locations
            if (lastLocations.isNullOrEmpty()) {
                historyLocationsResult.value = ApiResult.Success(emptyList())
                return@launch
            }
            cancelRunningLocations()
            historyLocationsResult.value = ApiResult.Success(lastLocations)
        }
    }

//    private fun getHistoryLocations(vehicleNumber: String) {
//        cancelRunningLocations()
//        historyLocationsResult.value = ApiResult.Success(MapsDummyRepository.getHistoryLocationData(vehicleNumber))
//    }

    fun pushLocations(locations: List<LocationsQuery.Location?>?, fromIndex: Int = 0) {
        if (locations.isNullOrEmpty()) {
            return
        }
        pushLocationJob = viewModelScope.launch {
            var index = fromIndex
            while (index < locations.size) {
                val location = locations[index]
                replayLocationCurrentIndex.postValue(index)
                vehicleHistoryMovingLocation.postValue(location)
                index++
                if (index == 1) {
                    //First need to slow to animate camera to location
                    delay(ReplayViewSpeed.NORMAL_3X)
                } else {
                    delay(replayViewSpeed)
                }
            }
        }
    }

    fun resumeReplayLocations(fromIndex: Int = 0) {
        pushLocations(replayLocations.value, fromIndex)
    }

    fun pauseReplayLocations() {
        cancelRunningLocations()
    }

    private fun cancelRunningLocations() {
        if (pushLocationJob != null) {
            pushLocationJob?.cancel()
        }
    }

    fun getVehicleList() {
        val list = ArrayList<String>()
        VehicleRepository.getVehicleList().forEach {
            if (it?.vehicleNum != null) {
                list.add(it.vehicleNum)
            }
        }
        vehicleNameListResult.value = list
    }

}