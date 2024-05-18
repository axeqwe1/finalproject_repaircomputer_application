package com.example.repaircomputerapplication_finalproject.screens.form.formManageData

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.repaircomputerapplication_finalproject.data.ScreenRoutes
import com.example.repaircomputerapplication_finalproject.model.*
import com.example.repaircomputerapplication_finalproject.viewModel.ManageViewModel.DataManageViewModel

@Composable
fun crudDataScreen(dataType: String?, navController: NavController, viewModel: DataManageViewModel = viewModel()) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    val buildingList = viewModel.building.collectAsState().value
    val departmentList = viewModel.department.collectAsState().value
    val equipmentList = viewModel.eq.collectAsState().value
    val equipmentTypeList = viewModel.eqc.collectAsState().value
    val loedList = viewModel.loed.collectAsState().value

    LaunchedEffect(dataType) {
        if (dataType != null) {
            viewModel.LoadData(dataType)
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(ScreenRoutes.DataForm.passIsEditAndId(isEdit = false, DataType = dataType ?: "null"))
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE0F7FA))
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("DataType: $dataType", fontSize = 24.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("ค้นหา") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            val dataList = when (dataType) {
                "Building" -> buildingList?.filterBySearchQuery(searchQuery.text, listOf(
                    {it.building_floor.toString()},
                    {it.building_name},
                    {it.building_id.toString()},
                    {it.building_room_number}
                )) ?: emptyList()
                "Department" -> departmentList?.filterBySearchQuery(searchQuery.text, listOf(
                    {it.department_id.toString()},
                    {it.departmentName}
                )) ?: emptyList()
                "Equipment" -> equipmentList?.filterBySearchQuery(searchQuery.text, listOf(
                    {it.eq_id.toString()},
                    {it.eq_name},
                    {it.eqc_id.toString()},
                    {it.eq_unit},
                    {it.eq_start_date},
                    {it.eq_status}
                )) ?: emptyList()
                "EquipmentType" -> equipmentTypeList?.filterBySearchQuery(searchQuery.text, listOf(
                    {it.eqc_id.toString()},
                    {it.eqc_name}
                ))  ?: emptyList()
                "LevelOfDamage" -> loedList?.filterBySearchQuery(searchQuery.text, listOf(
                    {it.loed_id.toString()},
                    {it.loed_Name}
                )) ?: emptyList()
                else -> emptyList()
            }

            LazyColumn {
                items(dataList) { item ->
                    when (dataType) {
                        "Building" -> BuildingItem(item as BuildingData, navController, viewModel)
                        "Department" -> DepartmentItem(item as DepartmentData, navController, viewModel)
                        "Equipment" -> EquipmentItem(item as EquipmentData, navController, viewModel)
                        "EquipmentType" -> EquipmentTypeItem(item as EquipmentTypeData, navController, viewModel)
                        "LevelOfDamage" -> LevelOfDamageItem(item as LevelOfDamageData, navController, viewModel)
                        else -> Text("Invalid data type", color = Color.Red, modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
    }
}

fun <T> List<T>.filterBySearchQuery(searchQuery: String, selectors: List<(T) -> String?>): List<T> {
    return this.filter { item ->
        selectors.any { selector ->
            selector(item)?.contains(searchQuery, ignoreCase = true) ?: false
        }
    }
}

@Composable
fun BuildingItem(item: BuildingData, navController: NavController, viewModel: DataManageViewModel) {
    Card(
        shape = RoundedCornerShape(10.dp),
        backgroundColor = Color(0xFFE0E0E0),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                // Navigate to detailed view or edit screen if needed
            }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(text = "${item.building_id}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "ห้อง: ${item.building_room_number}", fontSize = 14.sp)
            Text(text = "ชั้น: ${item.building_floor}", fontSize = 14.sp)
            Text(text = "ชื่อตึก: ${item.building_name}", fontSize = 14.sp)
        }
    }
}

@Composable
fun DepartmentItem(item: DepartmentData, navController: NavController, viewModel: DataManageViewModel) {
    Card(
        shape = RoundedCornerShape(10.dp),
        backgroundColor = Color(0xFFE0E0E0),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                // Navigate to detailed view or edit screen if needed
            }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(text = "${item.department_id.toString()}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "ชื่อหน่วยงาน: ${item.departmentName}", fontSize = 14.sp)
        }
    }
}

@Composable
fun EquipmentItem(item: EquipmentData, navController: NavController, viewModel: DataManageViewModel) {
    Card(
        shape = RoundedCornerShape(10.dp),
        backgroundColor = Color(0xFFE0E0E0),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                // Navigate to detailed view or edit screen if needed
            }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(text = "${item.eq_id}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "ชื่ออุปกรณ์: ${item.eq_name}", fontSize = 14.sp)
            Text(text = "สถานะ: ${item.eq_status}", fontSize = 14.sp)
            Text(text = "หน่วยนับ: ${item.eq_unit}", fontSize = 14.sp)
            Text(text = "วันเริ่มใช้งานอุปกรณ์: ${item.eq_start_date}", fontSize = 14.sp)
            Text(text = "รหัสประเภทอุปกรณ์: ${item.eqc_id}", fontSize = 14.sp)
        }
    }
}

@Composable
fun EquipmentTypeItem(item: EquipmentTypeData, navController: NavController, viewModel: DataManageViewModel) {
    Card(
        shape = RoundedCornerShape(10.dp),
        backgroundColor = Color(0xFFE0E0E0),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                // Navigate to detailed view or edit screen if needed
            }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(text = "${item.eqc_id}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "ชื่อประเภทอุปกรณ์: ${item.eqc_name}", fontSize = 14.sp)
        }
    }
}

@Composable
fun LevelOfDamageItem(item: LevelOfDamageData, navController: NavController, viewModel: DataManageViewModel) {
    Card(
        shape = RoundedCornerShape(10.dp),
        backgroundColor = Color(0xFFE0E0E0),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                // Navigate to detailed view or edit screen if needed
            }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(text = "${item.loed_id}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "ชื่อระดับความเสียหาย: ${item.loed_Name}", fontSize = 14.sp)
        }
    }
}
