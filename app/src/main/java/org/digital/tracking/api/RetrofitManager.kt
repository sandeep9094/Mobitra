package org.digital.tracking.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.digital.tracking.BuildConfig
import org.digital.tracking.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitManager {


    fun getApiService(authInterceptor: AuthorizationInterceptor): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.SERVER_URL_ON_BOARDING_API)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkHttpClient(authInterceptor))
            .build()
        return retrofit.create(ApiService::class.java)
    }

    private fun getOkHttpClient(authInterceptor: AuthorizationInterceptor): OkHttpClient {
        val logging = HttpLoggingInterceptor() // set your desired log level
        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        } else {
            logging.level = HttpLoggingInterceptor.Level.NONE
        }
        val okHttpClientBuilder = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
        return okHttpClientBuilder.build()
    }

}