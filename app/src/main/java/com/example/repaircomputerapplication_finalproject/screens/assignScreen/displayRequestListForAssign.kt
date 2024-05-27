package com.example.repaircomputerapplication_finalproject.screens.assignScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.repaircomputerapplication_finalproject.data.ScreenRoutes
import com.example.repaircomputerapplication_finalproject.viewModel.AssignWork.AssignWorkViewModel
import com.example.repaircomputerapplication_finalproject.viewModel.ContextDataStore.dataStore
import com.example.repaircomputerapplication_finalproject.viewModel.RequestForRepiarListViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.*

data class RepairRequest(
    val id: String,
    val equipmentName: String,
    val description: String,
    val building: String,
    val roomNumber: String,
    val floor: String,
    val department: String,
    val reporter: String,
    val assignee: String?,
    val date: Date
)

@Composable
fun displayRequestListForAssign(navController: NavHostController,viewModel:AssignWorkViewModel = viewModel()) {
    val backlogList = viewModel.backlogData.collectAsState().value
    var admin_id by remember { mutableStateOf("") }
    val context = LocalContext.current
    LaunchedEffect(backlogList){
        admin_id = context.dataStore.data.map {item ->
            item[stringPreferencesKey("userId")]
        }.first() ?: "null"
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD0E6F8)) // Background color similar to the image
            .padding(16.dp)
    ) {
        if (backlogList != null) {
            items(backlogList.data) { item ->
                OutlinedCard(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(
                                ScreenRoutes
                                .TechnicianListBacklog
                                .passAdminIdAndRrid(admin_id = admin_id, rrid = item.rrid.toString() )
                            ) },
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .background(Color.White) // Changed background to White for better contrast
                            .padding(16.dp)
                    ) {
                        Text(
                            text = item.rrid.toString(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = item.equipment.eq_name.toString(),
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
                                Text(text = "ชั้นตึก  : ${item.building.building_floor}")
                                Text(text = "เลขห้อง : ${item.building.building_room_number}")
                                Text(text = "หน่วยงาน: ${viewModel.getDepartmentName(item.employee.departmentId!!)}")
                            }
                            Column(modifier = Modifier.weight(1f)) {
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
                                Text(text = "ผู้แจ้ง : ${item.employee.firstname}   ${item.employee.lastname}")
                                Text(text = "ผู้รับงาน: ${"ไม่มี"}")
                                Text(text = "วันที่แจ้ง: ${formattedDate}")
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewRequestRepairScreen() {
    val navController = rememberNavController()
    displayRequestListForAssign(
        navController = navController,
    )
}
