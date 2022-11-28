package org.digital.tracking.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloException
import com.google.gson.Gson
import com.mobitra.tracking.DailyDistanceReportQuery
import com.mobitra.tracking.LastLocationsQuery
import com.mobitra.tracking.ReportsQuery
import com.mobitra.tracking.StopageReportQuery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.digital.tracking.di.ResourceProvider
import org.digital.tracking.model.ApiResult
import org.digital.tracking.model.DailyDistanceReportResponse
import org.digital.tracking.model.DailyReport
import org.digital.tracking.utils.*
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val apolloClient: ApolloClient,
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    private val reportStartDate = getMonthStartDate()
    private val reportEndDate = getMonthEndDate()
    val reportApiResult: MutableLiveData<ApiResult<List<ReportsQuery.Report?>>> = MutableLiveData()
    val haltReportApiResult: MutableLiveData<ApiResult<List<ReportsQuery.Report?>>> = MutableLiveData()
    val dailyDistanceReportResult: MutableLiveData<ApiResult<List<DailyReport>>> = MutableLiveData()
    val stopageReportApiResult: MutableLiveData<ApiResult<List<StopageReportQuery.StoppageReport?>>> = MutableLiveData()
    val totalDistanceReportApiResult: MutableLiveData<ApiResult<List<LastLocationsQuery.LastLocation?>>> = MutableLiveData()

    fun getReport(imeiNumber: String, fromDate: String = reportStartDate, toDate: String = reportEndDate) {
        reportApiResult.value = ApiResult.Loading
        viewModelScope.launch {
            val response = try {
                val query = ReportsQuery(imeiNumber, fromDate, toDate)
                apolloClient.query(query).execute()
            } catch (e: ApolloException) {
                reportApiResult.value = ApiResult.Error(resourceProvider.defaultError)
                return@launch
            }
            val reports = response.data?.reports
            if (reports.isNullOrEmpty()) {
                reportApiResult.value = ApiResult.Success(emptyList())
                return@launch
            }
            reportApiResult.value = ApiResult.Success(reports)
        }
    }

    fun getTotalDistanceReport(imeiNumber: String, fromDate: String = reportStartDate, toDate: String = reportEndDate) {
        totalDistanceReportApiResult.value = ApiResult.Loading
        viewModelScope.launch {
            val response = try {
                val lastLocationsQuery = LastLocationsQuery(
                    Optional.Absent, Optional.Present(listOf(imeiNumber)),
                    fromDate, toDate
                )
                apolloClient.query(lastLocationsQuery).execute()
            } catch (e: ApolloException) {
                totalDistanceReportApiResult.value = ApiResult.Error(resourceProvider.defaultError)
                return@launch
            }
            val lastLocations = response.data?.lastLocation
            if (lastLocations.isNullOrEmpty()) {
                totalDistanceReportApiResult.value = ApiResult.Error(resourceProvider.defaultError)
                return@launch
            }
            totalDistanceReportApiResult.value = ApiResult.Success(lastLocations)
        }
    }

    fun getHaltReport(imeiNumber: String, fromDate: String = reportStartDate, toDate: String = reportEndDate) {
        haltReportApiResult.value = ApiResult.Loading
        viewModelScope.launch {
            val response = try {
                val query = ReportsQuery(imeiNumber, fromDate, toDate)
                apolloClient.query(query).execute()
            } catch (e: ApolloException) {
                haltReportApiResult.value = ApiResult.Error(resourceProvider.defaultError)
                return@launch
            }
            val reports = response.data?.reports
            if (reports.isNullOrEmpty()) {
                haltReportApiResult.value = ApiResult.Success(emptyList())
                return@launch
            }
            haltReportApiResult.value = ApiResult.Success(reports)
        }
    }

    fun getDailyDistanceReport(imeiNumber: String, fromDate: String = getYesterdayDate(), toDate: String = getTodayDate()) {
        dailyDistanceReportResult.value = ApiResult.Loading
        viewModelScope.launch {
            val response = try {
                val query = DailyDistanceReportQuery(imeiNumber, fromDate, toDate)
                apolloClient.query(query).execute()
            } catch (e: ApolloException) {
                dailyDistanceReportResult.value = ApiResult.Error(resourceProvider.defaultError)
                return@launch
            }
            try {
                val responseString = Gson().toJson(response.data)
                Log.d("Reports", "Daily Distance Report: ${responseString}")
                val reports = Gson().fromJson(responseString, DailyDistanceReportResponse::class.java)
                if (reports.dailyReport.isNullOrEmpty()) {
                    Log.d("Reports", "Daily Distance Report isNullOrEmpty: ${responseString}")
                    dailyDistanceReportResult.value = ApiResult.Success(emptyList())
                    return@launch
                }
                Log.d("Reports", "Daily Distance Report: ${responseString}")
                dailyDistanceReportResult.value = ApiResult.Success(reports.dailyReport)
            } catch (exception: Exception) {
                Log.d("Reports", "Daily Distance Exception: ${exception}")
                dailyDistanceReportResult.value = ApiResult.Success(emptyList())
            }

        }
    }

    fun getStopageReports(imeiNumber: String, fromDate: String = reportStartDate, toDate: String = reportEndDate) {
        stopageReportApiResult.value = ApiResult.Loading
        viewModelScope.launch {
            val response = try {
                val query = StopageReportQuery(imeiNumber, fromDate, toDate)
                apolloClient.query(query).execute()
            } catch (e: ApolloException) {
                stopageReportApiResult.value = ApiResult.Error(resourceProvider.defaultError)
                return@launch
            }
            val reports = response.data?.stoppageReport
            if (reports.isNullOrEmpty()) {
                stopageReportApiResult.value = ApiResult.Success(emptyList())
                return@launch
            }
            stopageReportApiResult.value = ApiResult.Success(reports)
        }
    }

}