package com.example.repaircomputerapplication_finalproject.screens

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.repaircomputerapplication_finalproject.component.ToggleComponent
import com.example.repaircomputerapplication_finalproject.data.ScreenRoutes
import com.example.repaircomputerapplication_finalproject.viewModel.ContextDataStore.dataStore
import com.example.repaircomputerapplication_finalproject.viewModel.RequestForRepairViewModel.RequestForRepiarListViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun RequestRepairScreen(navController: NavHostController, viewModel: RequestForRepiarListViewModel = viewModel()) {
    val requestList = viewModel.requestList.collectAsState().value ?: emptyList()
    val eqtList = viewModel.eqtList.collectAsState().value ?: emptyList()
    val empList = viewModel.empList.collectAsState().value ?: emptyList()
    val buildList = viewModel.buildingList.collectAsState().value ?: emptyList()
    val departList = viewModel.departList.collectAsState().value ?: emptyList()
    val techList = viewModel.techList.collectAsState().value ?: emptyList()
    var userType by remember { mutableStateOf("") }
    var currentMode by remember { mutableStateOf("งานที่ยังไม่จ่าย") } // Track the current mode
    var searchQuery by remember { mutableStateOf("") }
    val context = LocalContext.current
    var isLoading by remember{ mutableStateOf(true) }
    LaunchedEffect(Unit) {
        viewModel.loadData()
        userType = context.dataStore.data.map { item ->
            item[stringPreferencesKey("role")]
        }.first() ?: "null"
    }
    LaunchedEffect(key1 = true) {
        delay(2000) // Delay for 3 seconds
        isLoading = false
    }
    if(isLoading){
        LoadingScreen() // Show loading screen
    }else{
        if (requestList.isEmpty() || eqtList.isEmpty() || empList.isEmpty() || buildList.isEmpty() || departList.isEmpty() || techList.isEmpty()) {
            Text(text = "ไม่มีคำขอ")
        } else {
            Log.d(TAG, "RequestRepairScreen: $requestList")
            Column(Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("ค้นหา") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
                ToggleComponent(role = userType) { mode ->
                    currentMode = mode
                    Log.d(TAG, "RequestRepairScreen: $currentMode")
                }

                // Filter the requestList based on the currentMode and searchQuery
                val filteredRequestList = requestList.filter { item ->
                    // Parse the ISO 8601 timestamp
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                    dateFormat.timeZone = TimeZone.getTimeZone("UTC")
                    val date: Date = try {
                        dateFormat.parse(item.timestamp) ?: Date()
                    } catch (e: Exception) {
                        Date()
                    }
                    // Format the date to a readable format
                    val displayFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                    val formattedDate = displayFormat.format(date)
                    val isInSearchQuery = searchQuery.isEmpty() ||
                            item.rrid.toString().contains(searchQuery, ignoreCase = true) ||
                            formattedDate.contains(searchQuery, ignoreCase = true) ||
                            viewModel.getDepartmentName(item.employee.departmentId ?: 0).contains(searchQuery, ignoreCase = true) ||
                            viewModel.getEmployeeFullName(item.employee_id ?: 0).contains(searchQuery, ignoreCase = true) ||
                            item.eq_id.toString().contains(searchQuery, ignoreCase = true)
                    val isInCurrentMode = when (userType) {
                        "Admin" -> if (currentMode == "งานที่ยังไม่จ่าย") item.receive_repair == null else true
                        "Technician" -> if (currentMode == "ไม่ได้กรอกข้อมูล") item.receive_repair.repair_details.isEmpty() else true
                        else -> true
                    }
                    isInSearchQuery && isInCurrentMode
                }

                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(filteredRequestList) { item ->
                        OutlinedCard(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate(
                                        ScreenRoutes.detailRepair.passRridAndUserType(
                                            item.rrid.toString(),
                                            userType
                                        )
                                    )
                                },
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .background(Color.White)
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = "รหัสงาน: ${item.rrid.toString()}",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "สถานะงาน: ${item.request_status}",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text ="ชื่ออุปกรณ์: ${ item.equipment.eq_name ?: "null" }",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = item.rr_description,
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = "ชื่อตึก  : ${viewModel.getBuildingName(item.building_id ?: 0)}")
                                        Text(text = "ชั้นตึก  : ${viewModel.getBuildingFloor(item.building_id ?: 0)}")
                                        Text(text = "เลขห้อง : ${viewModel.getBuildingRoom(item.building_id ?: 0)}")
                                        Text(text = "หน่วยงาน: ${viewModel.getDepartmentName(item.employee.departmentId ?: 0)}")
                                    }
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = "ผู้แจ้ง : ${viewModel.getEmployeeFullName(item.employee_id ?: 0)}")
                                        if(item.receive_repair != null){
                                            Text(text = "ผู้รับงาน: ${viewModel.getTechnicianName(item.receive_repair.tech_id ?: 0) ?: "ไม่มี"}")
                                        } else{
                                            Text(text = "ผู้รับงาน: ไม่มี")
                                        }
                                        // Parse the ISO 8601 timestamp
                                        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                                        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
                                        val date: Date = try {
                                            dateFormat.parse(item.timestamp) ?: Date()
                                        } catch (e: Exception) {
                                            Date()
                                        }

                                        // Format the date to a readable format
                                        val displayFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                                        val formattedDate = displayFormat.format(date)

                                        Text(text = "วันที่แจ้ง: ${formattedDate}")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


