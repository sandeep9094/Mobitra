package org.digital.tracking.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.exception.ApolloException
import com.mobitra.tracking.ReportsQuery
import com.mobitra.tracking.StopageReportQuery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.digital.tracking.di.ResourceProvider
import org.digital.tracking.model.ApiResult
import org.digital.tracking.repository.cache.UserCacheManager
import org.digital.tracking.utils.*
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
    val dailyDistanceReportResult: MutableLiveData<ApiResult<List<ReportsQuery.Report?>>> = MutableLiveData()
    val stopageReportApiResult: MutableLiveData<ApiResult<List<StopageReportQuery.StoppageReport?>>> = MutableLiveData()

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
                val query = ReportsQuery(imeiNumber, fromDate, toDate)
                apolloClient.query(query).execute()
            } catch (e: ApolloException) {
                dailyDistanceReportResult.value = ApiResult.Error(resourceProvider.defaultError)
                return@launch
            }
            val reports = response.data?.reports
            if (reports.isNullOrEmpty()) {
                dailyDistanceReportResult.value = ApiResult.Success(emptyList())
                return@launch
            }
            dailyDistanceReportResult.value = ApiResult.Success(reports)
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