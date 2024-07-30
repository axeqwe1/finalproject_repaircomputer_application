package com.example.repaircomputerapplication_finalproject.screens.reportscreen

import android.app.DatePickerDialog
import android.content.Context
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.repaircomputerapplication_finalproject.viewModel.AssignWork.AssignWorkViewModel
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

val sampleRepairs = listOf(
    RepairItem("01-01-2023", "Dell Inspiron", "Laptop", 5, "John Doe", 100),
    RepairItem("02-01-2023", "HP LaserJet", "Printer", 3, "Jane Smith", 100),
    RepairItem("03-01-2023", "Epson X123", "Projector", 10, "Mike Johnson", 100)
)

@Composable
fun ReportListScreen(navController: NavController,repairs: List<RepairItem>) {
    var startDate by remember { mutableStateOf("dd-mm-yyyy") }
    var endDate by remember { mutableStateOf("dd-mm-yyyy") }
    val context = LocalContext.current

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
                text = "Date",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f).wrapContentWidth(Alignment.CenterHorizontally)
            )
            Text(
                text = "Equipment",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f).wrapContentWidth(Alignment.CenterHorizontally)
            )
            Text(
                text = "Type",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f).wrapContentWidth(Alignment.CenterHorizontally)
            )
            Text(
                text = "Repair Count",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f).wrapContentWidth(Alignment.CenterHorizontally)
            )
            Text(
                text = "Technician",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f).wrapContentWidth(Alignment.CenterHorizontally)
            )
        }

        // Data Rows
        LazyColumn {
            items(repairs) { repair ->
                val backgroundColor = if (repair.repairCount > 5) Color(0xFFFFCDD2) else Color.White
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(backgroundColor)
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = repair.reportDate, fontSize = 12.sp, modifier = Modifier.weight(1f))
                    Text(text = repair.equipmentName, fontSize = 12.sp, modifier = Modifier.weight(1f))
                    Text(text = repair.equipmentType, fontSize = 12.sp, modifier = Modifier.weight(1f))
                    Text(text = repair.repairCount.toString(), fontSize = 12.sp, modifier = Modifier.weight(1f))
                    Text(text = repair.technicianName, fontSize = 12.sp, modifier = Modifier.weight(1f))
                }
            }
        }

        // Total Requests Component
        TotalRequestsComponent(totalRequests = repairs.firstOrNull()?.totalRequestCount ?: 0)
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

@Preview(showBackground = true)
@Composable
fun RepairTablePreview() {
    val nav = rememberNavController()
    ReportListScreen(nav,repairs = sampleRepairs)
}