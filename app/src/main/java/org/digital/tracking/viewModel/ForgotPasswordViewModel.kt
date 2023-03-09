package org.digital.tracking.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import org.digital.tracking.R
import org.digital.tracking.api.ApiService
import org.digital.tracking.di.ResourceProvider
import org.digital.tracking.model.ApiResult
import org.digital.tracking.model.ChangePasswordResponse
import org.digital.tracking.model.PasswordGenerateOtpResponse
import org.digital.tracking.model.VerifyOtpResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val apiService: ApiService,
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    val verifyOtpApiResponse: MutableLiveData<ApiResult<Boolean>> = MutableLiveData()
    val forgotPasswordApiResult: MutableLiveData<ApiResult<Boolean>> = MutableLiveData()
    val changePasswordApiResponse: MutableLiveData<ApiResult<Boolean>> = MutableLiveData()

    fun forgotPassword(userName: String) {
        forgotPasswordApiResult.value = ApiResult.Loading
        val payload = JsonObject()
        payload.addProperty("username", userName)
        val call = apiService.generatePasswordOtp(payload)
        call.enqueue(object : Callback<PasswordGenerateOtpResponse> {
            override fun onResponse(call: Call<PasswordGenerateOtpResponse>, response: Response<PasswordGenerateOtpResponse>) {
                if (response.body() == null) {
                    forgotPasswordApiResult.postValue(ApiResult.Error(resourceProvider.getString(R.string.error_message_failed_to_send_otp)))
                    return
                }
                response.body()?.let {
                    if (it.payload.otpSent) {
                        forgotPasswordApiResult.postValue(ApiResult.Success(true))
                        return
                    }
                    forgotPasswordApiResult.postValue(ApiResult.Error(resourceProvider.getString(R.string.error_message_failed_to_send_otp)))
                }
            }

            override fun onFailure(call: Call<PasswordGenerateOtpResponse>, t: Throwable) {
                forgotPasswordApiResult.postValue(
                    ApiResult.Error(
                        t.localizedMessage ?: resourceProvider.getString(R.string.error_message_failed_to_send_otp)
                    )
                )
            }

        })
    }

    fun verifyOtp(username: String, otp: String) {
        verifyOtpApiResponse.value = ApiResult.Loading
        val payload = JsonObject()
        payload.addProperty("username", username)
        payload.addProperty("otp", otp)
        val call = apiService.verifyPasswordOtp(payload)
        call.enqueue(object : Callback<VerifyOtpResponse> {
            override fun onResponse(call: Call<VerifyOtpResponse>, response: Response<VerifyOtpResponse>) {
                if (response.body() == null) {
                    verifyOtpApiResponse.postValue(ApiResult.Error(resourceProvider.getString(R.string.error_message_wrong_otp)))
                    return
                }
                response.body()?.let {
                    if (it.payload.otpValidated) {
                        verifyOtpApiResponse.postValue(ApiResult.Success(true))
                        return
                    }
                    verifyOtpApiResponse.postValue(ApiResult.Error(resourceProvider.getString(R.string.error_message_wrong_otp)))
                }
            }

            override fun onFailure(call: Call<VerifyOtpResponse>, t: Throwable) {
                verifyOtpApiResponse.postValue(
                    ApiResult.Error(
                        t.localizedMessage ?: resourceProvider.getString(R.string.error_message_wrong_otp)
                    )
                )
            }
        })
    }

    fun changePassword(username: String, newPassword: String) {
        changePasswordApiResponse.value = ApiResult.Loading
        val payload = JsonObject()
        payload.addProperty("username", username)
        payload.addProperty("password", newPassword)
        val call = apiService.changePassword(payload)
        call.enqueue(object : Callback<ChangePasswordResponse> {
            override fun onResponse(call: Call<ChangePasswordResponse>, response: Response<ChangePasswordResponse>) {
                if (response.body() == null) {
                    changePasswordApiResponse.postValue(ApiResult.Error(resourceProvider.getString(R.string.error_message_password_change)))
                    return
                }
                response.body()?.let {
                    if (it.payload.passwordChanged) {
                        changePasswordApiResponse.postValue(ApiResult.Success(true))
                        return
                    }
                    changePasswordApiResponse.postValue(ApiResult.Error(resourceProvider.getString(R.string.error_message_password_change)))
                }
            }

            override fun onFailure(call: Call<ChangePasswordResponse>, t: Throwable) {
                changePasswordApiResponse.postValue(
                    ApiResult.Error(
                        t.localizedMessage ?: resourceProvider.getString(R.string.error_message_password_change)
                    )
                )
            }
        })
    }

}