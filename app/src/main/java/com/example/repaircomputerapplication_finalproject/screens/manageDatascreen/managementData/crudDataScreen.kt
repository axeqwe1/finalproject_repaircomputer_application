package com.example.repaircomputerapplication_finalproject.screens.form.formManageData

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
    val techStatusList = viewModel.techStatus.collectAsState().value
    val loedList = viewModel.loed.collectAsState().value
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
                    {
                        equipmentTypeList?.firstOrNull() { items ->
                            items.eqc_id == it.eqc_id
                        }?.eqc_name
                    }
                )) ?: emptyList()
                "EquipmentType" -> equipmentTypeList?.filterBySearchQuery(searchQuery.text, listOf(
                    {it.eqc_id.toString()},
                    { it.eqc_name },
                ))  ?: emptyList()
                "LevelOfDamage" -> loedList?.filterBySearchQuery(searchQuery.text, listOf(
                    {it.loed_id.toString()},
                    {it.loed_Name}
                )) ?: emptyList()
                "TechStatus" -> techStatusList?.filterBySearchQuery(searchQuery.text,listOf(
                    {it.status_id.toString()},
                    {it.receive_request_status.toString()}
                )) ?: emptyList()
                else -> emptyList()
            }

            LazyColumn {
                items(dataList) { item ->
                    when (dataType) {
                        "Building" -> BuildingItem(dataType,item as BuildingData, navController, viewModel)
                        "Department" -> DepartmentItem(dataType,item as DepartmentData, navController, viewModel)
                        "Equipment" -> EquipmentItem(dataType,item as EquipmentData, navController, viewModel)
                        "EquipmentType" -> EquipmentTypeItem(dataType,item as EquipmentTypeData, navController, viewModel)
                        "LevelOfDamage" -> LevelOfDamageItem(dataType,item as LevelOfDamageData, navController, viewModel)
                        "TechStatus" -> TechStatusItem(dataType,item as techStatusData, navController, viewModel)
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BuildingItem(dataType: String?,item: BuildingData, navController: NavController, viewModel: DataManageViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    ConfirmDeleteDialog(
        showDialog = showDialog,
        onDismiss = { showDialog = false },
        onConfirm = {
            if(dataType != null && item.building_id != null){
                viewModel.deleteData(dataType,item.building_id)
                showDialog = false
            }
        }
    )
    Card(
        shape = RoundedCornerShape(10.dp),
        backgroundColor = Color(0xFFE0E0E0),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .combinedClickable(
                onClick = {
                    navController.navigate(
                        ScreenRoutes.DataForm.passIsEditAndId(
                            true,
                            item.building_id.toString(),
                            dataType ?: "null"
                        )
                    )
                },
                onLongClick = { showDialog = true }
            )
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DepartmentItem(dataType: String?,item: DepartmentData, navController: NavController, viewModel: DataManageViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    ConfirmDeleteDialog(
        showDialog = showDialog,
        onDismiss = { showDialog = false },
        onConfirm = {
            if(dataType != null && item.department_id != null){
                viewModel.deleteData(dataType,item.department_id)
                showDialog = false
            }
        }
    )
    Card(
        shape = RoundedCornerShape(10.dp),
        backgroundColor = Color(0xFFE0E0E0),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .combinedClickable(
                onClick = {
                    navController.navigate(
                        ScreenRoutes.DataForm.passIsEditAndId(
                            true,
                            item.department_id.toString(),
                            dataType ?: "null"
                        )
                    )
                },
                onLongClick = { showDialog = true }
            )
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EquipmentItem(dataType: String?,Eqitem: EquipmentData, navController: NavController, viewModel: DataManageViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    ConfirmDeleteDialog(
        showDialog = showDialog,
        onDismiss = { showDialog = false },
        onConfirm = {
            if(dataType != null && Eqitem.eq_id != null){
                viewModel.deleteData(dataType,Eqitem.eq_id)
                showDialog = false
            }
        }
    )
    var eqcName by remember { mutableStateOf("") }
    LaunchedEffect(Eqitem){
        eqcName = viewModel.getEquipmentTypeName(Eqitem.eqc_id ?: 0)
        Log.d(TAG, "EquipmentItem: ${Eqitem.eqc_id}")
    }

    Card(
        shape = RoundedCornerShape(10.dp),
        backgroundColor = Color(0xFFE0E0E0),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .combinedClickable(
                onClick = {
                    navController.navigate(
                        ScreenRoutes.DataForm.passIsEditAndId(
                            true,
                            Eqitem.eq_id.toString(),
                            dataType ?: "null"
                        )
                    )
                },
                onLongClick = { showDialog = true }
            )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(text = "${Eqitem.eq_id}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "ชื่ออุปกรณ์: ${Eqitem.eq_name}", fontSize = 14.sp)
            Text(text = "สถานะ: ${Eqitem.eq_status}", fontSize = 14.sp)
            Text(text = "หน่วยนับ: ${Eqitem.eq_unit}", fontSize = 14.sp)
            Text(text = "วันเริ่มใช้งานอุปกรณ์: ${Eqitem.eq_start_date}", fontSize = 14.sp)
            Text(text = "ชื่อประเภทอุปกรณ์: $eqcName", fontSize = 14.sp)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EquipmentTypeItem(dataType: String?,item: EquipmentTypeData, navController: NavController, viewModel: DataManageViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    ConfirmDeleteDialog(
        showDialog = showDialog,
        onDismiss = { showDialog = false },
        onConfirm = {
            if(dataType != null && item.eqc_id != null){
                viewModel.deleteData(dataType,item.eqc_id)
                showDialog = false
            }
        }
    )
    Card(
        shape = RoundedCornerShape(10.dp),
        backgroundColor = Color(0xFFE0E0E0),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .combinedClickable(
                onClick = {
                    navController.navigate(
                        ScreenRoutes.DataForm.passIsEditAndId(
                            true,
                            item.eqc_id.toString(),
                            dataType ?: "null"
                        )
                    )
                },
                onLongClick = { showDialog = true }
            )
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LevelOfDamageItem(dataType: String?,item: LevelOfDamageData, navController: NavController, viewModel: DataManageViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    ConfirmDeleteDialog(
        showDialog = showDialog,
        onDismiss = { showDialog = false },
        onConfirm = {
            if(dataType != null && item.loed_id != null){
                viewModel.deleteData(dataType,item.loed_id)
                showDialog = false
            }
        }
    )
    Card(
        shape = RoundedCornerShape(10.dp),
        backgroundColor = Color(0xFFE0E0E0),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .combinedClickable(
                onClick = {
                    navController.navigate(
                        ScreenRoutes.DataForm.passIsEditAndId(
                            true,
                            item.loed_id.toString(),
                            dataType ?: "null"
                        )
                    )
                },
                onLongClick = { showDialog = true }
            )
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
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TechStatusItem(dataType: String?,item: techStatusData, navController: NavController, viewModel: DataManageViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    ConfirmDeleteDialog(
        showDialog = showDialog,
        onDismiss = { showDialog = false },
        onConfirm = {
            if(dataType != null && item.status_id != null){
                viewModel.deleteData(dataType,item.status_id)
                showDialog = false
            }
        }
    )
    Card(
        shape = RoundedCornerShape(10.dp),
        backgroundColor = Color(0xFFE0E0E0),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .combinedClickable(
                onClick = {
                    navController.navigate(
                        ScreenRoutes.DataForm.passIsEditAndId(
                            true,
                            item.status_id.toString(),
                            dataType ?: "null"
                        )
                    )
                },
                onLongClick = { showDialog = true }
            )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(text = "${item.status_id}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "ชื่อระดับความเสียหาย: ${item.receive_request_status}", fontSize = 14.sp)
        }
    }
}
@Composable
fun ConfirmDeleteDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = "Confirm Delete") },
            text = { Text(text = "Are you sure you want to delete this item?") },
            confirmButton = {
                Button(onClick = onConfirm) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        )
    }
}