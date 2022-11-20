package org.digital.tracking.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloException
import com.mobitra.tracking.LastLocationsQuery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.digital.tracking.di.ResourceProvider
import org.digital.tracking.model.ApiResult
import org.digital.tracking.repository.VehicleRepository
import org.digital.tracking.repository.cache.UserCacheManager
import org.digital.tracking.utils.*
import javax.inject.Inject

@HiltViewModel
class VehicleViewModel @Inject constructor(
    private val apolloClient: ApolloClient,
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    var selectedVehicle: LastLocationsQuery.LastLocation? = null

    val allVehicleLocationApiResult: MutableLiveData<ApiResult<List<LastLocationsQuery.LastLocation?>>> = MutableLiveData()
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

    fun fetchAllVehicleLocations() {
        allVehicleLocationApiResult.value = ApiResult.Loading
        viewModelScope.launch {
            val startDate = getMonthStartDate()
            val endDate = getMonthEndDate()
            val imeiNumbers = arrayListOf<String>(UserCacheManager.fallbackImei)
            val response = try {
                val lastLocationsQuery = LastLocationsQuery(
                    Optional.Absent, Optional.Present(imeiNumbers),
                    startDate, endDate
                )
                apolloClient.query(lastLocationsQuery).execute()
            } catch (e: ApolloException) {
                allVehicleLocationApiResult.value = ApiResult.Error(resourceProvider.defaultError)
                return@launch
            }
            val vehiclesLastLocations = response.data?.lastLocation
            if (vehiclesLastLocations.isNullOrEmpty()) {
                allVehicleLocationApiResult.value = ApiResult.Success(emptyList())
                return@launch
            }
            allVehicleLocationApiResult.value = ApiResult.Success(vehiclesLastLocations)
        }
    }

}