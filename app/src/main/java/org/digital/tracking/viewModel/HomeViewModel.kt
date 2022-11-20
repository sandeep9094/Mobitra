package org.digital.tracking.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloException
import com.google.gson.Gson
import com.mobitra.tracking.GetVehiclesQuery
import com.mobitra.tracking.LastLocationsQuery
import com.mobitra.tracking.VehiclesAverageRunningQuery
import com.mobitra.tracking.VehiclesWeeklyAverageRunningQuery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.digital.tracking.api.ApiService
import org.digital.tracking.di.ResourceProvider
import org.digital.tracking.manager.SharedPrefs
import org.digital.tracking.model.ApiResult
import org.digital.tracking.model.DeviceListItem
import org.digital.tracking.model.GetUserResponse
import org.digital.tracking.repository.VehicleRepository
import org.digital.tracking.repository.cache.UserCacheManager
import org.digital.tracking.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val apiService: ApiService,
    private val sharedPrefs: SharedPrefs,
    private val apolloClient: ApolloClient,
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    //TODO move these viewModels to activity
    val deviceListResult: MutableLiveData<ApiResult<List<DeviceListItem>>> = MutableLiveData()
    val vehicleWeeklyKms: MutableLiveData<ApiResult<List<VehiclesWeeklyAverageRunningQuery.VehiclesWeeklyAverageRunning?>>> =
        MutableLiveData()
    val averageRunningKmResult: MutableLiveData<ApiResult<List<VehiclesAverageRunningQuery.VehiclesAverageRunning?>>> = MutableLiveData()
    val vehicleList: MutableLiveData<ApiResult<List<LastLocationsQuery.LastLocation?>>> = MutableLiveData()

    fun getDeviceList() {
        deviceListResult.value = ApiResult.Loading
        if (UserCacheManager.getDeviceList().isNotEmpty()) {
            deviceListResult.value = ApiResult.Success(UserCacheManager.getDeviceList())
            return
        }
        val userId = sharedPrefs.userId
        val call = apiService.getUser(userId)
//        Timber.d("Get User Url  : ${call.request().url}")
        call.enqueue(object : Callback<GetUserResponse> {
            override fun onResponse(call: Call<GetUserResponse>, response: Response<GetUserResponse>) {
//                Timber.d("GetUser: onResponse : ${response.body()}")
                if (response.body() == null) {
                    deviceListResult.postValue(ApiResult.Error(resourceProvider.defaultError))
                    return
                }
                val deviceList = response.body()?.payload?.deviceList
                if (deviceList.isNullOrEmpty()) {
                    deviceListResult.postValue(ApiResult.Error(resourceProvider.defaultError))
                    return
                }
                deviceListResult.postValue(ApiResult.Success(deviceList))
            }

            override fun onFailure(call: Call<GetUserResponse>, t: Throwable) {
                deviceListResult.postValue(ApiResult.Error(t.localizedMessage ?: resourceProvider.defaultError))
            }

        })
    }

    fun getVehicleWeeklyKms(vehicleImei: String) {
        vehicleWeeklyKms.value = ApiResult.Loading
        viewModelScope.launch {
            val response = try {
                VehiclesAverageRunningQuery
                val query = VehiclesWeeklyAverageRunningQuery(vehicleImei)
                Timber.d("Vehicle IMEI : $vehicleImei")
                apolloClient.query(query).execute()
            } catch (e: ApolloException) {
                vehicleWeeklyKms.value = ApiResult.Error(resourceProvider.defaultError)
                return@launch
            }
            val vehiclesWeeklyAverageRunningKms = response.data?.vehiclesWeeklyAverageRunning
            Timber.d("vehiclesWeeklyAvgRunningKms: ${Gson().toJson(vehiclesWeeklyAverageRunningKms)}")
            if (vehiclesWeeklyAverageRunningKms.isNullOrEmpty()) {
                vehicleWeeklyKms.value = ApiResult.Success(emptyList())
                return@launch
            }
            vehicleWeeklyKms.value = ApiResult.Success(vehiclesWeeklyAverageRunningKms)
        }
    }

    fun fetchAverageKms(vehicleImei: String) {
        averageRunningKmResult.value = ApiResult.Loading
        viewModelScope.launch {
            val response = try {
                VehiclesAverageRunningQuery
                val query = VehiclesAverageRunningQuery(vehicleImei)
                apolloClient.query(query).execute()
            } catch (e: ApolloException) {
                averageRunningKmResult.value = ApiResult.Error(resourceProvider.defaultError)
                return@launch
            }
            val vehiclesAverageRunningKms = response.data?.vehiclesAverageRunning
            if (vehiclesAverageRunningKms.isNullOrEmpty()) {
                averageRunningKmResult.value = ApiResult.Success(emptyList())
                return@launch
            }
            averageRunningKmResult.value = ApiResult.Success(vehiclesAverageRunningKms)
        }
    }

    fun fetchAllVehicles() {
        if (UserCacheManager.fallbackImei.isEmpty()) {
            fetchUser(callingFromFetchVehicles = true)
            return
        }
        //List Fragment Data
        val startDate = getTodayDate(isStartDate = true)
        val endDate = getTodayDate()
        vehicleList.value = ApiResult.Loading
        viewModelScope.launch {
            val imeiNumbers = UserCacheManager.getDeviceImeiList()
            val response = try {
                val lastLocationsQuery = LastLocationsQuery(
                    Optional.Absent, Optional.Present(imeiNumbers),
                    startDate, endDate
                )
                apolloClient.query(lastLocationsQuery).execute()
            } catch (e: ApolloException) {
                vehicleList.value = ApiResult.Error(resourceProvider.defaultError)
                return@launch
            }
            val lastLocations = response.data?.lastLocation
            if (lastLocations.isNullOrEmpty()) {
                vehicleList.value = ApiResult.Error(resourceProvider.defaultError)
                return@launch
            }
            vehicleList.value = ApiResult.Success(lastLocations)
        }
    }

    fun fetchUser(callingFromFetchVehicles: Boolean) {
        val userId = sharedPrefs.userId
        val call = apiService.getUser(userId)
        Timber.d("Get User Url  : ${call.request().url}")
        call.enqueue(object : Callback<GetUserResponse> {
            override fun onResponse(call: Call<GetUserResponse>, response: Response<GetUserResponse>) {
                Timber.d("GetUser: onResponse : ${response.body()}")
                if (response.body() == null) {
                    return
                }
                response.body()?.let {
                    if (!it.status.isValidResponse()) {
                        return
                    }
                    UserCacheManager.setUser(it.payload, sharedPrefs)
//                    if (callingFromFetchVehicles) {
                    fetchAllVehicles()
//                    } else {
//                        getVehicleList()
//                    }
                }
            }

            override fun onFailure(call: Call<GetUserResponse>, t: Throwable) {
            }
        })
    }

}