package org.digital.tracking.api

import com.google.gson.JsonObject
import okhttp3.ResponseBody
import org.digital.tracking.model.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("/authenticate")
    fun loginUser(@Body body: JsonObject): Call<LoginResponse>

    @POST("api/user")
    fun createUser(@Body body: JsonObject): Call<SignUpResponse>

    @POST("api/user/{userId}/device")
    fun addDevice(@Path("userId") userId: String, @Body body: JsonObject): Call<AddDeviceResponse>

    @GET("api/user/{userId}/devices")
    fun getDeviceList(@Path("userId") userId: String): Call<List<DeviceListItem>>

    @GET("api/user/{userId}")
    fun getUser(@Path("userId") userId: String): Call<GetUserResponse>

    @POST("generate-validate-otp")
    fun generatePasswordOtp(@Body payload: JsonObject): Call<PasswordGenerateOtpResponse>

    @POST("generate-validate-otp")
    fun verifyPasswordOtp(@Body payload: JsonObject): Call<VerifyOtpResponse>

    @POST("change-password")
    fun changePassword(@Body payload: JsonObject): Call<ChangePasswordResponse>

    //sandeeptest11@gmail.com
    //12345678
}