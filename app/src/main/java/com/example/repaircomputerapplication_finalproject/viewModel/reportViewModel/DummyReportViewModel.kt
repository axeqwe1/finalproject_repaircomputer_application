package com.example.repaircomputerapplication_finalproject.viewModel.reportViewModel

import androidx.lifecycle.ViewModel
import com.example.repaircomputerapplication_finalproject.screens.reportscreen.RepairReport
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DummyReportViewModel : ViewModel() {
    private val _reportList = MutableStateFlow<List<RepairReport>>(
        listOf(
            RepairReport("Laptop", 5, "Electronics", "John Doe", 10),
            RepairReport("Printer", 3, "Electronics", "Jane Smith", 7)
        )
    )
    val reportList: StateFlow<List<RepairReport>> = _reportList

    fun exportReport() {
        // No-op for preview
    }
}
