package org.digital.tracking.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.digital.tracking.R
import org.digital.tracking.api.ApiService
import org.digital.tracking.api.PayloadHelper
import org.digital.tracking.di.ResourceProvider
import org.digital.tracking.manager.SharedPrefs
import org.digital.tracking.model.AddDeviceResponse
import org.digital.tracking.model.ApiResult
import org.digital.tracking.repository.cache.UserCacheManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AddDeviceViewModel @Inject constructor(
    private val apiService: ApiService,
    private val sharedPrefs: SharedPrefs,
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    val addDeviceApiResult: MutableLiveData<ApiResult<Boolean>> = MutableLiveData()

    fun addDevice(imei: String, simNumber: String, vehicleType: String, device: String) {
        addDeviceApiResult.value = ApiResult.Loading
        val payload = PayloadHelper.addNewDevicePayload(imei, simNumber, vehicleType, device)
        val call = apiService.addDevice(sharedPrefs.userId, payload)
        call.enqueue(object : Callback<AddDeviceResponse> {
            override fun onResponse(call: Call<AddDeviceResponse>, response: Response<AddDeviceResponse>) {
                if (response.body() == null) {
                    addDeviceApiResult.postValue(ApiResult.Error(resourceProvider.defaultError))
                    return
                }
                val newDevice = response.body()?.payload
                if (newDevice == null) {
                    addDeviceApiResult.postValue(ApiResult.Error(resourceProvider.getString(R.string.error_message_failed_to_add_new_device)))
                    return
                }
                addDeviceApiResult.postValue(ApiResult.Success(true))
                UserCacheManager.setDeviceList(emptyList()) // To clear device list cache
            }

            override fun onFailure(call: Call<AddDeviceResponse>, t: Throwable) {
                addDeviceApiResult.postValue(ApiResult.Error(t.localizedMessage ?: resourceProvider.defaultError))
            }

        })

    }

}