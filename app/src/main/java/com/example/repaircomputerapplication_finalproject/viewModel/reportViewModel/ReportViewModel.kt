package com.example.repaircomputerapplication_finalproject.viewModel.reportViewModel

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.repaircomputerapplication_finalproject.api_service.RetrofitInstance
import com.example.repaircomputerapplication_finalproject.model.DashboardModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class ReportViewModel(application: Application) : AndroidViewModel(application) {
    private val _dashboardData = MutableStateFlow<DashboardModel?>(null)
    val dashboardData: StateFlow<DashboardModel?> = _dashboardData
//    private val _reportList = MutableStateFlow<DashboardModel?>(null)
//    val reportList: StateFlow<DashboardModel?> = _reportList

    init {
        loadData()
    }
    fun loadData(){
        viewModelScope.launch {
            _dashboardData.value = fetchDashboardData()
        }
    }
    suspend fun fetchDashboardData(): DashboardModel {
        // Simulate fetching data
        val response = RetrofitInstance.apiService.getDashboardData()
        if(response.isSuccessful && response.body() != null){
            Log.d(TAG, "fetchDashboardData: ${response.body()}")
            return response.body()!!
        }else{
            throw Exception("Fail to Fetch DashboardData")
        }
    }

    fun exportReport() {
        viewModelScope.launch {
            // Call backend API to export CSV
            // Example using Ktor client or Retrofit
        }
    }
}
