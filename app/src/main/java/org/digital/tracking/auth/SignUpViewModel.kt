package org.digital.tracking.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import dagger.hilt.android.lifecycle.HiltViewModel
import org.digital.tracking.R
import org.digital.tracking.api.ApiService
import org.digital.tracking.di.ResourceProvider
import org.digital.tracking.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val apiService: ApiService,
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    val registerUserResponseResult: MutableLiveData<ResponseResult<SignUpResponse>> = MutableLiveData()
    var deviceListResponse: MutableLiveData<ResponseResult<List<DeviceListItem>>> = MutableLiveData()

    fun registerUser(user: CreateUser) {
        registerUserResponseResult.value = ResponseResult.InProgress
        val payload = Gson().toJson(user)
        val jsonObject: JsonObject = JsonParser().parse(payload).asJsonObject
        val call = apiService.createUser(jsonObject)
        Timber.d("Payload Json : ${jsonObject}")
        call.enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                response.body()?.let {
                    registerUserResponseResult.value = ResponseResult.Success(it)
                    return
                }
                registerUserResponseResult.value =
                    ResponseResult.Error(resourceProvider.getString(R.string.error_message_user_already_exist))
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                registerUserResponseResult.value = ResponseResult.Error("Failed to create user")
            }

        })
    }

    fun getDeviceList() {
//        deviceListResponse.value = ResponseResult.InProgress
//        val call = apiService.getDeviceList()
//        call.enqueue(object : Callback<List<DeviceListItem>> {
//            override fun onResponse(call: Call<List<DeviceListItem>>, response: Response<List<DeviceListItem>>) {
//                response.body()?.let {
//                    deviceListResponse.value = ResponseResult.Success(it)
//                    return
//                }
//                deviceListResponse.value = ResponseResult.Success(emptyList())
//            }
//
//            override fun onFailure(call: Call<List<DeviceListItem>>, t: Throwable) {
//                deviceListResponse.value = ResponseResult.Error(t.localizedMessage ?: "Something Went wrong!")
//            }
//
//        })
    }

}