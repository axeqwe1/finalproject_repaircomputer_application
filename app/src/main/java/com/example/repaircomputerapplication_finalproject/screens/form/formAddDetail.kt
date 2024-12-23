package com.example.repaircomputerapplication_finalproject.screens.form

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.repaircomputerapplication_finalproject.model.detailRepairData
import com.example.repaircomputerapplication_finalproject.viewModel.RequestForRepairViewModel.formAddDetailViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun formAddDetail(
    rrid: String,
    _rd_id: String,
    isUpdate: Boolean,
    navController: NavController,
    viewModel: formAddDetailViewModel = viewModel()
) {
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
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val requestStatus = listOf(
        "กำลังดำเนินการ",
        "เสร็จสิ้นแล้ว",
        "กำลังส่งคืน",
        "ส่งคืนเสร็จสิ้น",
        "ไม่สามารถซ่อมได้",
        "ไม่พบว่าชำรุด"
    )

    LaunchedEffect(key1 = true){
        viewModel.LoadData()
        requestData = viewModel.fetchRequestForRepairData(rrid.toInt())
    }
    LaunchedEffect(key1 = damageLevels,key2 = requestData) {
        try {
            if (isUpdate) {
                if (requestData?.receive_repair?.repair_details?.isNotEmpty() == true) {
                    val lastDetail = requestData?.receive_repair?.repair_details?.lastOrNull()
                    repairDetail = if (lastDetail?.rd_description.toString().split(":").size > 1) {
                        requestData?.receive_repair!!.repair_details.lastOrNull()?.rd_description.toString().split(":")[1]
                    } else {
                        ""
                    }
                    val level = damageLevels?.find { it.loed_id == lastDetail?.loed_id }
                    if (level != null) {
                        selectedDamageLevel = level.loed_Name.orEmpty()
                        Loed_ID = level.loed_id.toString()
                        Log.d(TAG, "Updated Selected Damage Level: $selectedDamageLevel")
                    } else {
                        Log.d(TAG, "Damage level not found for loed_id: ${lastDetail?.loed_id}")
                    }
                    lastDetail?.let { detail ->
                        Loed_ID = detail.loed_id?.toString() ?: ""
                        selectRequestText = requestData?.request_status.orEmpty()
                        Log.d(TAG, "formAddDetail: Updated Damage Level = $selectedDamageLevel")
                    }
                }
            } else {
                if (requestData?.receive_repair?.repair_details?.isNotEmpty() == true) {
                    val lastDetail = requestData?.receive_repair?.repair_details?.lastOrNull()
                    lastDetail?.let { detail ->
                        Loed_ID = detail.loed_id?.toString() ?: ""
                        selectedDamageLevel = damageLevels?.find { it.loed_id == detail.loed_id }?.loed_Name.orEmpty()
                        selectRequestText = requestData?.request_status.orEmpty()

                        Log.d(TAG, "formAddDetail: Loed_ID = $Loed_ID")
                    }
                } else {
                    Log.d(TAG, "formAddDetail: No details found")
                }
            }
            departmentName = viewModel.getDepartmentName(requestData?.employee?.departmentId ?: 0)
        }catch (e:Exception){
            Log.e(TAG, "formAddDetail: Error fetching data: ${e.message}", )
            errorMessage = "เกิดข้อผิดพลาดในการโหลดข้อมูล"
            isError = true // เปิดสถานะข้อผิดพลาด
        }finally {
            isLoading = false // ปิดสถานะการโหลดเมื่อเสร็จสิ้น (ทั้งสำเร็จและผิดพลาด)
        }

    }
    LaunchedEffect(selectRequestText){
        if (selectRequestText == "เสร็จสิ้นแล้ว") {
            repairDetail = "การซ่อมเสร็จสิ้นแล้ว"
        }
        if (selectRequestText == "กำลังส่งคืน") {
            repairDetail = "กำลังส่งคืนอุปกรณ์"
        }
        if (selectRequestText == "ส่งคืนเสร็จสิ้น") {
            repairDetail = ""
        }
    }
    fun recordData() {
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
    }

    fun validateMode(mode: Int) {
        val detail = requestData?.receive_repair!!.repair_details
        if (mode == 1) {
            if (repairDetail.isBlank() || selectedDamageLevel == "เลือกระดับความเสียหาย" || selectRequestText == "เลือกสถานะของการซ่อม") {
                validationError = "กรุณากรอกข้อมูลให้ครบถ้วน"
            } else {
                recordData()
                showDialog = false
            }
        }
        if (mode == 2) {
            if(isUpdate){
                if (repairDetail.isBlank() || selectRequestText == "เลือกสถานะของการซ่อม" || selectedDamageLevel == "เลือกระดับความเสียหาย") {
                    validationError = "กรุณากรอกข้อมูลให้ครบถ้วน"
                } else {
                    recordData()
                    showDialog = false
                }
            }else if(detail.isEmpty()){
                if (repairDetail.isBlank() || selectRequestText == "เลือกสถานะของการซ่อม" || selectedDamageLevel == "เลือกระดับความเสียหาย") {
                    validationError = "กรุณากรอกข้อมูลให้ครบถ้วน"
                } else {
                    recordData()
                    showDialog = false
                }
            }else{
                if (repairDetail.isBlank() || selectRequestText == "เลือกสถานะของการซ่อม") {
                    validationError = "กรุณากรอกข้อมูลให้ครบถ้วน"
                } else {
                    recordData()
                    showDialog = false
                }
            }
        }
    }

    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD0E6F8)) // Background color
            .padding(16.dp)
            .verticalScroll(scrollState),
    ) {
        if(isLoading){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

        }else if (isError){
            // แสดง Error Message เมื่อมีข้อผิดพลาด
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = errorMessage, color = Color.Red, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { isLoading = true }) {
                    Text("ลองอีกครั้ง")
                }
            }
        }
        else
        {
            if (showDialog) {
                CustomAlertDialog(
                    onConfirm = {
                        coroutineScope.launch {
                            if (selectRequestText != "เสร็จสิ้นแล้ว" && selectRequestText != "กำลังส่งคืน" && selectRequestText != "ส่งคืนเสร็จสิ้น" && isUpdate) {
                                validateMode(1)
                            }else{
                                validateMode(2)
                            }
                        }
                    },
                    onDismiss = { showDialog = false }
                )
            }

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

            if (selectRequestText != "เสร็จสิ้นแล้ว" && selectRequestText != "กำลังส่งคืน" && selectRequestText != "ส่งคืนเสร็จสิ้น") {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(text = "รายละเอียดการซ่อม", fontSize = 16.sp)
                    TextField(
                        value = repairDetail,
                        onValueChange = { repairDetail = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .background(Color.White, RoundedCornerShape(8.dp)),
                        singleLine = false,
                        maxLines = 4
                    )
                    if (repairDetail.isBlank()) {
                        Text(text = "กรุณากรอกรายละเอียดการซ่อม", color = Color.Red, fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            } else if (selectRequestText == "ส่งคืนเสร็จสิ้น") {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(text = "ชื่อผู้รับคืน", fontSize = 16.sp)
                    TextField(
                        value = repairDetail,
                        onValueChange = { repairDetail = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .background(Color.White, RoundedCornerShape(8.dp)),
                        singleLine = false,
                        maxLines = 4
                    )
                    if (repairDetail.isBlank()) {
                        Text(text = "กรุณากรอกชื่อผู้รับคืน", color = Color.Red, fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            if (isUpdate || requestData?.receive_repair?.repair_details?.lastOrNull() == null) {
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
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpand) },
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
                                    text = { Text(level.loed_Name ?: "") },
                                    onClick = {
                                        selectedDamageLevel = level.loed_Name.orEmpty()
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
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (validationError != null) {
                showDialog = false
                AlertDialog(
                    onDismissRequest = { validationError = null }, // ปิด Dialog เมื่อผู้ใช้คลิกนอก Dialog
                    title = {
                        Text(text = "เกิดข้อผิดพลาด")
                    },
                    text = {
                        Text(text = validationError ?: "An unknown error occurred")
                    },
                    confirmButton = {
                        Button(
                            onClick = { validationError = null } // ปิด Dialog เมื่อผู้ใช้กด OK
                        ) {
                            Text("OK")
                        }
                    }
                )
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
