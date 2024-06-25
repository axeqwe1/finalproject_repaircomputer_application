package com.example.repaircomputerapplication_finalproject.screens.reportscreen

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.repaircomputerapplication_finalproject.viewModel.reportViewModel.ReportViewModel
import kotlinx.coroutines.launch

data class RepairReport(
    val equipmentName: String,
    val repairCount: Int,
    val equipmentType: String,
    val technicianName: String,
    val totalRequests: Int
)

@Composable
fun ReportScreen(viewModel: ReportViewModel = viewModel()) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val reportList by viewModel.reportList.collectAsState(initial = emptyList())

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(reportList) { report ->
                ReportItem(report)
            }
        }
    }
}

@Composable
fun ReportItem(report: RepairReport) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Equipment: ${report.equipmentName}")
            Text(text = "Repair Count: ${report.repairCount}")
            Text(text = "Equipment Type: ${report.equipmentType}")
            Text(text = "Technician: ${report.technicianName}")
            Text(text = "Total Requests: ${report.totalRequests}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewReportScreen() {
    val dummyViewModel = ReportViewModel(Application())
    ReportScreen(viewModel = dummyViewModel)
}
