package com.example.repaircomputerapplication_finalproject.screens.reportscreen

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.repaircomputerapplication_finalproject.viewModel.HomeViewModel
import com.example.repaircomputerapplication_finalproject.viewModel.reportViewModel.ReportViewModel
import com.example.repaircomputerapplication_finalproject.data.ScreenRoutes
import com.example.repaircomputerapplication_finalproject.viewModel.ContextDataStore.dataStore
import com.example.repaircomputerapplication_finalproject.viewModel.LogoutResult
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: ReportViewModel = viewModel(),homeViewModel: HomeViewModel = viewModel(),navController: NavController) {
    val dashboardData = viewModel.dashboardData.collectAsState().value
    val totalTasks = remember { mutableStateOf(0) }
    val pendingTasks = remember { mutableStateOf(0) }
    val receiveTask= remember { mutableStateOf(0) }
    val completedTasks = remember { mutableStateOf(0) }
    val context = LocalContext.current
    var role = remember { mutableStateOf("") }
    val logoutResult by homeViewModel.logoutResult.collectAsState(initial = null)
    LaunchedEffect(context) {
        role.value = context.dataStore.data.map { preferences ->
            preferences[stringPreferencesKey("role")] ?: ""
        }.first()
    }
    LaunchedEffect(logoutResult) {
        when (logoutResult) {
            is LogoutResult.IsLogout -> navController.navigate(ScreenRoutes.AuthNav.route) {
                popUpTo(ScreenRoutes.HomeNav.route) { inclusive = true }
            }
            is LogoutResult.Failure -> Toast.makeText(context, "Login failed: ${(logoutResult as LogoutResult.Failure).error}", Toast.LENGTH_LONG).show()
            else -> {}
        }
    }
    LaunchedEffect(dashboardData){
        viewModel.loadData()
        if(dashboardData != null){
            totalTasks.value = dashboardData.TotalWork ?: 0
            pendingTasks.value = dashboardData.Backlog ?: 0
            receiveTask.value = dashboardData.ReceiveWork ?: 0
            completedTasks.value = dashboardData.SuccessWork ?: 0
        }
    }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFB3E5FC))
        ) {
            TopStats(completedTasks.value, pendingTasks.value, receiveTask.value)
            Spacer(modifier = Modifier.height(16.dp))
            MiddleStats(totalTasks.value)
            Spacer(modifier = Modifier.height(3.dp))
            BottomStats(context,pendingTasks.value, totalTasks.value, receiveTask.value)

        }
}

@Composable
fun TopStats(totalTasks: Int, pendingTasks: Int, receiveTask: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        StatCard("งานที่เสร็จสิ้น", totalTasks.toString(), Color(0xFF2EFF38), Modifier.weight(1f))
        Spacer(modifier = Modifier.width(8.dp))
        StatCard("งานค้าง", pendingTasks.toString(), Color(0xFFFFC107), Modifier.weight(1f))
        Spacer(modifier = Modifier.width(8.dp))
        StatCard("รับงานแล้ว", receiveTask.toString(), Color(0xFF4CAF50), Modifier.weight(1f))
    }
}

@Composable
fun StatCard(title: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        modifier = modifier
            .height(80.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = value,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun MiddleStats(completeTaskCount:Int) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color(0xFFB3E5FC))
                .padding(start = 16.dp, end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            StatCard("งานทั้งหมด", completeTaskCount.toString(), Color(0xFF03DAC5), Modifier.weight(1f))
            Spacer(modifier = Modifier.height(16.dp))
            // Add your graph or activity summary here
        }
    }
}

@Composable
fun BottomStats(context: Context,pendingOrders: Int, products: Int, customers: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFB3E5FC))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ExportReportComponent(context)
//        BottomStatCard("Pending Orders", pendingOrders.toString(), Color(0xFF6200EE))
//        BottomStatCard("Products", products.toString(), Color(0xFF03DAC5))
//        BottomStatCard("Customers", customers.toString(), Color(0xFFFFC107))
    }
}

@Composable
fun BottomStatCard(title: String, value: String, color: Color) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        modifier = Modifier
            .size(width = 100.dp, height = 100.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(text = value, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 24.sp)
        }
    }
}

@Composable
fun ExportReportComponent(context: Context, viewModel: ReportViewModel = viewModel()) {
    val calendar = Calendar.getInstance()

    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    val datePickerDialogStart = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            startDate = "$dayOfMonth-${month + 1}-$year"
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
    )

    val datePickerDialogEnd = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            endDate = "$dayOfMonth-${month + 1}-$year"
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp) // เพิ่มความใกล้ชิดกันขององค์ประกอบ
    ) {
        Text(
            text = "ออกรายงาน",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center, // จัดให้อยู่ตรงกลาง
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(end = 8.dp) // เพิ่ม padding ด้านขวาเพื่อให้ปุ่มใกล้กันขึ้น
            ) {
                Text("จากวันที่", fontWeight = FontWeight.Bold)
                Button(
                    modifier = Modifier.width(150.dp),
                    onClick = { datePickerDialogStart.show() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0)),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(text = if (startDate.isNotEmpty()) startDate else "จากวันที่", color = Color.White)
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(start = 8.dp) // เพิ่ม padding ด้านซ้ายเพื่อให้ปุ่มใกล้กันขึ้น
            ) {
                Text("ถึงวันที่", fontWeight = FontWeight.Bold)
                Button(
                    modifier = Modifier.width(150.dp),
                    onClick = { datePickerDialogEnd.show() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0)),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(text = if (endDate.isNotEmpty()) endDate else "ถึงวันที่", color = Color.White)
                }
            }
        }

        Button(
            onClick = {
                if (isDateValid(startDate, endDate)) {
                    // Implement export report logic
                    viewModel.exportReport(context,startDate,endDate)
                } else {
                    dialogMessage = "เลือกเวลาให้ถูกต้อง"
                    showDialog = true
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
            modifier = Modifier
                .padding(top = 24.dp)
                .width(200.dp)
                .height(50.dp),
            shape = RoundedCornerShape(50)
        ) {
            Text(text = "ออกกรายงาน", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("แจ้งเตือน!!!") },
                text = { Text(dialogMessage) },
                confirmButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

private fun isDateValid(startDate: String, endDate: String): Boolean {
    if (startDate.isEmpty() || endDate.isEmpty()) return true

    val dateFormat = java.text.SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return try {
        val start = dateFormat.parse(startDate)
        val end = dateFormat.parse(endDate)
        start.before(end) || start.equals(end)
    } catch (e: Exception) {
        false
    }
}
//@RequiresApi(Build.VERSION_CODES.O)
//@Preview(showBackground = true)
//@Composable
//fun ReportScreenPreview() {
//    ReportScreen()
//}
