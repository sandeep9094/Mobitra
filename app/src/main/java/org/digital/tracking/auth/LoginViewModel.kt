package org.digital.tracking.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.digital.tracking.R
import org.digital.tracking.api.ApiService
import org.digital.tracking.api.PayloadHelper
import org.digital.tracking.api.RetrofitManager
import org.digital.tracking.di.ResourceProvider
import org.digital.tracking.model.ApiResult
import org.digital.tracking.model.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val apiService: ApiService,
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    val loginResult: MutableLiveData<ApiResult<LoginResponse>> = MutableLiveData()

    fun loginUserWithEmail(email: String, password: String) {
        loginImpl(email, password)
    }

    fun loginUserWithPhone(phone: String, password: String) {
        loginImpl(phone, password)
    }

    private fun loginImpl(emailOrPhone: String, password: String) {
        loginResult.postValue(ApiResult.Loading)
        val payload = PayloadHelper.loginPayload(emailOrPhone, password)
//        val apiService = RetrofitManager.getApiService()
        val call = apiService.loginUser(payload)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.body() == null) {
                    loginResult.postValue(ApiResult.Error(resourceProvider.getString(R.string.error_message_invalid_credentials)))
                    return
                }
                response.body()?.let {
                    if (it.token.isEmpty()) {
                        loginResult.postValue(ApiResult.Error(resourceProvider.defaultError))
                        return
                    }
                    loginResult.postValue(ApiResult.Success(it))
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                loginResult.postValue(ApiResult.Error(t.localizedMessage ?: resourceProvider.getString(R.string.error_message_default)))
            }

        })
    }

}