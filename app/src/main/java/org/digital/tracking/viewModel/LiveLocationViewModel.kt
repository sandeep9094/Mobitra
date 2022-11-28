package org.digital.tracking.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloException
import com.google.android.material.snackbar.Snackbar
import com.mobitra.tracking.LastLocationsQuery
import com.mobitra.tracking.ReceiveLocationSubscription
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.launch
import org.digital.tracking.di.ResourceProvider
import org.digital.tracking.model.ApiResult
import org.digital.tracking.repository.MapsDummyRepository
import org.digital.tracking.repository.VehicleRepository
import org.digital.tracking.repository.cache.UserCacheManager
import org.digital.tracking.utils.*
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LiveLocationViewModel @Inject constructor(
    private val apolloClient: ApolloClient,
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    val liveLocationError: MutableLiveData<String> = MutableLiveData()
    val vehicleNameListResult: MutableLiveData<ArrayList<String>> = MutableLiveData(ArrayList())
    val newLocation: MutableLiveData<ReceiveLocationSubscription.ReceiveLocation> = MutableLiveData()
    val lastLocationApiResult: MutableLiveData<ApiResult<List<LastLocationsQuery.LastLocation?>>> = MutableLiveData()

    fun fetchLocation(vehicleNumber: String) {
        lastLocationApiResult.value = ApiResult.Loading
        viewModelScope.launch {
            val startDate = getMonthStartDate()
            val endDate = getMonthEndDate()
            val vehicleNumbers = arrayListOf<String>(vehicleNumber)
            val response = try {
                val lastLocationsQuery = LastLocationsQuery(
                    Optional.Present(vehicleNumbers), Optional.Absent,
                    startDate, endDate
                )
                apolloClient.query(lastLocationsQuery).execute()
            } catch (e: ApolloException) {
                lastLocationApiResult.value = ApiResult.Error(resourceProvider.defaultError)
                return@launch
            }
            val lastLocations = response.data?.lastLocation
            if (lastLocations.isNullOrEmpty()) {
                lastLocationApiResult.value = ApiResult.Success(emptyList())
                return@launch
            }
            lastLocationApiResult.value = ApiResult.Success(lastLocations)
        }
    }

    fun getLiveLocation(vehicleImei: String) {
        viewModelScope.launch {
            Timber.d("LiveLocation: fetch location vehicleImei: $vehicleImei")
            apolloClient.subscription(ReceiveLocationSubscription(Optional.Absent, Optional.Present(vehicleImei))).toFlow()
                .retryWhen { _, attempt ->
                    liveLocationError.postValue("LiveLocation: Vehicle is out of coverage area, Please try again after some time!")
                    delay(attempt * 1000)
                    true
                }
                .collect {
                    val location = it.data?.receiveLocation
                    location?.let {
                        newLocation.postValue(it)
                        Timber.d("LiveLocation: Push new location : [${location.latitude}, ${location.longitude}]")
                    }
                }
        }
    }

    fun pushDummyLocation(vehicleNumber: String) {
//        val locations = MapsDummyRepository.getReceiveLocationData(vehicleNumber)
//        viewModelScope.launch {
//            locations.forEach {
//                newLocation.postValue(it)
//                Timber.tag("LiveLocationActivity").d("pushDummyLocation: ${it.latitude},${it.longitude}")
//                delay(3000)
//            }
//        }
    }

}