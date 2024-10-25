package com.example.repaircomputerapplication_finalproject.screens.reportscreen

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.repaircomputerapplication_finalproject.model.RepairReport
import com.example.repaircomputerapplication_finalproject.utils.formatTimestamp
import com.example.repaircomputerapplication_finalproject.viewModel.AssignWork.AssignWorkViewModel
import com.example.repaircomputerapplication_finalproject.viewModel.reportViewModel.ReportViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.*


data class RepairItem(
    val reportDate: String,
    val equipmentName: String,
    val equipmentType: String,
    val repairCount: Int,
    val technicianName: String,
    val totalRequestCount: Int
)


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReportListScreen(navController: NavController, viewModel: ReportViewModel = viewModel()) {
    var startDate by remember { mutableStateOf("DD-MM-YYYY") }
    var endDate by remember { mutableStateOf("DD-MM-YYYY") }
    val repairData = viewModel.repairData.collectAsState().value ?: emptyList()
    val context = LocalContext.current

    LaunchedEffect(startDate, endDate) {
        if (startDate.isNotEmpty() && endDate.isNotEmpty() && startDate != "DD-MM-YYYY" && endDate != "DD-MM-YYYY") {
            viewModel.fetchReportData(startDate, endDate)
        }
    }

    // Function to show date picker
    fun showDatePicker(context: Context, onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selectedDate = String.format("%02d-%02d-%04d", dayOfMonth, month + 1, year)
                onDateSelected(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD0E6F8)) // Background color similar to the image
            .padding(16.dp)
    ) {
        // Date Picker Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { showDatePicker(context) { startDate = it } }) {
                Text(text = "จากวันที่: $startDate")
            }
            Button(onClick = { showDatePicker(context) { endDate = it } }) {
                Text(text = "ถึงวันที่: $endDate")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Header Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "วัน/เดือน/ปี ที่แจ้งซ่อม",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
            Text(
                text = "ชื่ออุปกรณ์",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
            Text(
                text = "สถานะ",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
            Text(
                text = "จำนวนการซ่อม",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
            Text(
                text = "ช่างผู้รับงาน",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
        }

        // Data Rows with limited height
        Box(modifier = Modifier
            .weight(1f)
            .heightIn(max = 300.dp) // limit the height of LazyColumn
        ) {
            LazyColumn {
                items(repairData) { repair ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (repair != null) {
                            Text(
                                text = formatTimestamp(repair.timestamp).split(',')[0],
                                fontSize = 10.sp,
                                modifier = Modifier.weight(1f).wrapContentWidth(Alignment.CenterHorizontally)
                            )
                        }
                        Spacer(modifier = Modifier.padding(vertical = 5.dp))
                        Text(text = "${repair!!.eq_name}(${repair!!.eqc_name})", fontSize = 10.sp, modifier = Modifier.weight(1f).wrapContentWidth(Alignment.CenterHorizontally))
                        Text(text = repair!!.request_status, fontSize = 10.sp, modifier = Modifier.weight(1f).wrapContentWidth(Alignment.CenterHorizontally))
                        Text(text = repair!!.repair_count.toString(), fontSize = 10.sp, modifier = Modifier.weight(1f).wrapContentWidth(Alignment.CenterHorizontally))
                        if (repair.firstname.isNullOrBlank() || repair.firstname.isNullOrBlank()) {
                            Text(text = "ไม่มีผู้รับงาน", fontSize = 10.sp, modifier = Modifier.weight(1f).wrapContentWidth(Alignment.CenterHorizontally))
                        } else {
                            Text(text = "${repair!!.firstname} ${repair!!.lastname}", fontSize = 10.sp, modifier = Modifier.weight(1f).wrapContentWidth(Alignment.CenterHorizontally))
                        }
                    }
                }
            }
        }

        // Total Requests Component
        if (repairData.isNotEmpty()) {
            TotalRequestsComponent(totalRequests = repairData[0]?.totalRequests ?: 0)
        } else {
            TotalRequestsComponent(totalRequests = 0)
        }
    }
}

@Composable
fun TotalRequestsComponent(totalRequests: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.LightGray),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Total Requests in System", fontWeight = FontWeight.Bold)
        Text(text = totalRequests.toString(), fontWeight = FontWeight.Bold)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun RepairTablePreview() {
    val nav = rememberNavController()
    ReportListScreen(nav)
}