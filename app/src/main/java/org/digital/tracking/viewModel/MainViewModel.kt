package org.digital.tracking.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.digital.tracking.api.ApiService
import org.digital.tracking.di.AppModule
import org.digital.tracking.di.ResourceProvider
import org.digital.tracking.manager.SharedPrefs
import org.digital.tracking.model.ApiResult
import org.digital.tracking.model.DeviceListItem
import org.digital.tracking.model.GetUserResponse
import org.digital.tracking.model.User
import org.digital.tracking.repository.cache.UserCacheManager
import org.digital.tracking.utils.isValidResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val apiService: ApiService,
    var sharedPrefs: SharedPrefs,
    var resourceProvider: ResourceProvider
) : ViewModel() {

    val userResult: MutableLiveData<ApiResult<GetUserResponse>> = MutableLiveData()

    fun fetchUser() {
        val userId = sharedPrefs.userId
        val call = apiService.getUser(userId)
        Timber.d("Get User Url  : ${call.request().url}")
        call.enqueue(object : Callback<GetUserResponse> {
            override fun onResponse(call: Call<GetUserResponse>, response: Response<GetUserResponse>) {
                Timber.d("GetUser: onResponse : ${response.body()}")
                if (response.code() == 403) {
                    userResult.postValue(ApiResult.Error(resourceProvider.sessionExpired))
                    return
                }
                if (response.body() == null) {
                    userResult.postValue(ApiResult.Error(resourceProvider.defaultError))
                    return
                }
                response.body()?.let {
                    if (!it.status.isValidResponse()) {
                        userResult.postValue(ApiResult.Error(it.message))
                        return
                    }
                    UserCacheManager.setUser(it.payload, sharedPrefs)
                    userResult.postValue(ApiResult.Success(it))
                }
            }

            override fun onFailure(call: Call<GetUserResponse>, t: Throwable) {
                userResult.postValue(ApiResult.Error(t.localizedMessage ?: resourceProvider.defaultError))
            }
        })
    }

}