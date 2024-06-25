package com.example.repaircomputerapplication_finalproject.viewModel.reportViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.repaircomputerapplication_finalproject.screens.reportscreen.RepairReport
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class ReportViewModel(application: Application) : AndroidViewModel(application) {
    private val _reportList = MutableStateFlow<List<RepairReport>>(emptyList())
    val reportList: StateFlow<List<RepairReport>> = _reportList

    init {
        fetchReportData()
    }

    private fun fetchReportData() {
        // Simulate fetching data
        _reportList.value = listOf(
            RepairReport("Printer", 5, "Office Equipment", "John Doe", 20),
            RepairReport("Scanner", 3, "Office Equipment", "Jane Smith", 15)
        )
    }

    fun exportReport() {
        viewModelScope.launch {
            // Call backend API to export CSV
            // Example using Ktor client or Retrofit
        }
    }
}
