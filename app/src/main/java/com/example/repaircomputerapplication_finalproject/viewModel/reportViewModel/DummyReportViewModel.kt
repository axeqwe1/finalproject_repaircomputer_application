package com.example.repaircomputerapplication_finalproject.viewModel.reportViewModel

import android.app.Application
import com.example.repaircomputerapplication_finalproject.screens.reportscreen.RepairReport
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PreviewReportViewModel(application: Application) : ReportViewModel(application) {
    private val _mockReportList = MutableStateFlow(
        listOf(
            RepairReport("Laptop", 5, "Electronics", "John Doe", 10),
            RepairReport("Printer", 3, "Electronics", "Jane Smith", 7)
        )
    )

    override val reportList: StateFlow<List<RepairReport>>
        get() = _mockReportList
}
