package com.example.repaircomputerapplication_finalproject.screens.form

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.repaircomputerapplication_finalproject.model.detailRepairData
import com.example.repaircomputerapplication_finalproject.viewModel.RequestForRepairViewModel.formAddDetailViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun formAddDetail(rrid: String, _rd_id: String, isUpdate: Boolean, navController: NavController, viewModel: formAddDetailViewModel = viewModel()) {
    var repairDetail by remember { mutableStateOf("") }
    var selectedDamageLevel by remember { mutableStateOf("เลือกระดับความเสียหาย") }
    var Loed_ID by remember { mutableStateOf("") }
    var requestData by remember { mutableStateOf<detailRepairData?>(null) }
    var isExpand by remember { mutableStateOf(false) }
    var loedIsExpand by remember { mutableStateOf(false) }
    val damageLevels by viewModel.loedData.collectAsState()
    var selectRequestText by remember { mutableStateOf("เลือกสถานะของการซ่อม") }
    val coroutineScope = rememberCoroutineScope()
    var departmentName by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var validationError by remember { mutableStateOf<String?>(null) }
    val requestStatus = listOf(
        "กำลังส่งการแจ้งซ่อม",
        "กำลังดำเนินการ",
        "เสร็จสิ้นแล้ว",
        "ไม่สามารถซ่อมได้",
        "ไม่พบว่าชำรุด"
    )

    LaunchedEffect(key1 = true) {
        requestData = viewModel.fetchRequestForRepairData(rrid.toInt())
        if (isUpdate) {
            repairDetail = requestData?.receive_repair!!.repair_details.lastOrNull()?.rd_description.toString().split(":")[1]
            selectedDamageLevel = damageLevels?.find { it.loed_id == requestData?.receive_repair!!.repair_details.lastOrNull()?.loed_id }?.loed_Name.orEmpty()
            Loed_ID = damageLevels?.find { it.loed_id == requestData?.receive_repair!!.repair_details.lastOrNull()?.loed_id }?.loed_id.toString()
            selectRequestText = requestData?.request_status.toString()
        }
        departmentName = viewModel.getDepartmentName(requestData?.employee?.departmentId ?: 0)
        viewModel.LoadData()
    }

    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD0E6F8)) // Background color
            .padding(16.dp)
            .verticalScroll(scrollState),
    ) {
        if (showDialog) {
            CustomAlertDialog(
                onConfirm = {
                    coroutineScope.launch {
                        if (repairDetail.isBlank() || selectedDamageLevel == "เลือกระดับความเสียหาย" || selectRequestText == "เลือกสถานะของการซ่อม") {
                            validationError = "กรุณากรอกข้อมูลให้ครบถ้วน"
                        } else {
                            if (isUpdate) {
                                viewModel.UpdateDetail(
                                    _rd_id.toInt(),
                                    Loed_ID,
                                    "${selectRequestText}:${repairDetail}",
                                    selectRequestText
                                )
                                navController.navigateUp()
                            } else {
                                viewModel.AddDetail(
                                    requestData?.receive_repair?.rrce_id.toString(),
                                    Loed_ID,
                                    "${selectRequestText}:${repairDetail}",
                                    selectRequestText
                                )
                                navController.navigateUp()
                            }
                            showDialog = false
                        }
                    }
                },
                onDismiss = { showDialog = false }
            )
        }

        Text(text = "rrid: $rrid  rd_id: $_rd_id  isUpdate: $isUpdate")
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = "รหัสคำขอแจ้งซ่อม : $rrid", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "อุปกรณ์: (${requestData?.equipment?.eq_id}) ${requestData?.equipment?.eq_name}", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "ชื่อผู้แจ้ง: ${requestData?.employee?.firstname} ${requestData?.employee?.lastname}", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "หน่วยงาน: $departmentName", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "ตึก: ${requestData?.building?.building_name} ห้อง: ${requestData?.building?.building_room_number} ชั้นตึก: ${requestData?.building?.building_floor}", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "รายละเอียดอาการเสีย :")
                TextField(
                    value = "${requestData?.rr_description}",
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    readOnly = true
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "รายละเอียดการซ่อม", fontSize = 16.sp)
            TextField(
                value = repairDetail,
                onValueChange = { repairDetail = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp) // Set height for TextArea
                    .background(Color.White, RoundedCornerShape(8.dp)),
                singleLine = false,
                maxLines = 4
            )
            if (repairDetail.isBlank()) {
                Text(text = "กรุณากรอกรายละเอียดการซ่อม", color = Color.Red, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "ระดับความเสียหาย", fontSize = 16.sp)
            ExposedDropdownMenuBox(
                expanded = isExpand,
                onExpandedChange = { isExpand = it }
            ) {
                OutlinedTextField(
                    value = selectedDamageLevel,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("เลือกระดับความเสียหาย") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpand)
                    },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = isExpand,
                    onDismissRequest = { isExpand = false }
                ) {
                    damageLevels?.forEach { level ->
                        DropdownMenuItem(
                            text = { Text(text = level.loed_Name.toString()) },
                            onClick = {
                                selectedDamageLevel = level.loed_Name.toString()
                                Loed_ID = level.loed_id.toString()
                                isExpand = false
                            }
                        )
                    }
                }
            }
            if (selectedDamageLevel == "เลือกระดับความเสียหาย") {
                Text(text = "กรุณาเลือกระดับความเสียหาย", color = Color.Red, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "สถานะคำขอ", fontSize = 16.sp)
            ExposedDropdownMenuBox(
                expanded = loedIsExpand,
                onExpandedChange = { loedIsExpand = it }
            ) {
                OutlinedTextField(
                    value = selectRequestText,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("เลือกสถานะของการซ่อม") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = loedIsExpand)
                    },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = loedIsExpand,
                    onDismissRequest = { loedIsExpand = false }
                ) {
                    requestStatus.forEach { request ->
                        DropdownMenuItem(
                            text = { Text(text = request) },
                            onClick = {
                                selectRequestText = request
                                loedIsExpand = false
                            }
                        )
                    }
                }
            }
            if (selectRequestText == "เลือกสถานะของการซ่อม") {
                Text(text = "กรุณาเลือกสถานะของการซ่อม", color = Color.Red, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (validationError != null) {
            Text(text = validationError!!, color = Color.Red, fontSize = 12.sp)
        }

        Button(
            onClick = { showDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("บันทึก")
        }
    }
}

@Composable
fun CustomAlertDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(text = "Confirm Action")
        },
        text = {
            Text("Are you sure you want to proceed?")
        },
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

@Preview(showBackground = true)
@Composable
fun RepairDetailScreenPreview() {
    val rrid = "1"
    val rd_id = "1"
    val navController = rememberNavController()
    formAddDetail(rrid, rd_id, false, navController)
}
