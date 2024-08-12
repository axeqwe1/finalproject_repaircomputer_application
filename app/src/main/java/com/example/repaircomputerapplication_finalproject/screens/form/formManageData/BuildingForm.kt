package com.example.repaircomputerapplication_finalproject.screens.form.formManageData

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.repaircomputerapplication_finalproject.viewModel.ManageViewModel.DataManageViewModel

@Composable
fun buildingForm(isEdit: Boolean?, DataID: String?, viewModel: DataManageViewModel) {
    val context = LocalContext.current
    var roomNumber by remember { mutableStateOf("") }
    var floor by remember { mutableStateOf("") }
    var buildingName by remember { mutableStateOf("") }
    var btnName by remember { mutableStateOf("เพิ่มข้อมูล") }
    var showDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showValidationDialog by remember { mutableStateOf(false) }
    var alertMessage by remember { mutableStateOf("") }
    var validationError by remember { mutableStateOf("") }
    val buildList = viewModel.building.collectAsState().value

    LaunchedEffect(DataID, isEdit) {
        viewModel.LoadData()
    }

    LaunchedEffect(buildList) {
        val build = buildList?.find { it.building_id.toString() == DataID }
        Log.d(TAG, "buildingForm: $buildList DataId $DataID")
        if (build != null && DataID != null && isEdit == true) {
            roomNumber = build.building_room_number
            floor = build.building_floor.toString()
            buildingName = build.building_name
            btnName = "แก้ไขข้อมูล"
        }
    }

    fun validateForm(): Boolean {
        return when {
            roomNumber.isBlank() -> {
                validationError = "กรุณากรอกเลขห้อง"
                showValidationDialog = true
                false
            }
            floor.isBlank() -> {
                validationError = "กรุณากรอกชั้นตึก"
                showValidationDialog = true
                false
            }
            buildingName.isBlank() -> {
                validationError = "กรุณากรอกชื่อตึก"
                showValidationDialog = true
                false
            }
            else -> true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB3E5FC))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "เลขห้อง")
        TextField(
            value = roomNumber,
            onValueChange = { roomNumber = it },
            placeholder = { Text("เลขห้อง") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "ชั้นตึก")
        TextField(
            value = floor,
            onValueChange = {
                if (it.all { char -> char.isDigit() && it.length <= 3}) {
                    floor = it
                }
            },
            placeholder = { Text("ชั้นตึก") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "ชื่อตึก")
        TextField(
            value = buildingName,
            onValueChange = { buildingName = it },
            placeholder = { Text("ชื่อตึก") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (validateForm()) {
                    showDialog = true
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF8BC34A))
        ) {
            Text(text = btnName, fontSize = 20.sp, color = Color.Black)
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = if (isEdit == true) "ยืนยันการแก้ไข" else "ยืนยันการเพิ่มข้อมูล") },
            text = { Text(text = if (isEdit == true) "คุณต้องการแก้ไขข้อมูลหรือไม่?" else "คุณต้องการเพิ่มข้อมูลหรือไม่?") },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        if (isEdit == true) {
                            if (DataID != null) {
                                viewModel.editBuilding(DataID.toInt(), roomNumber, floor, buildingName)
                                showSuccessDialog = true
                                alertMessage = "แก้ไขข้อมูลสำเร็จ"
                            } else {
                                Toast.makeText(context, "not have DataId", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            viewModel.addBuilding(roomNumber, floor, buildingName)
                            showSuccessDialog = true
                            alertMessage = "เพิ่มข้อมูลสำเร็จ"
                        }
                    }
                ) {
                    Text("ยืนยัน")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("ยกเลิก")
                }
            }
        )
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text("สำเร็จ") },
            text = { Text(alertMessage) },
            confirmButton = {
                Button(onClick = { showSuccessDialog = false }) {
                    Text("OK")
                }
            }
        )
    }

    if (showValidationDialog) {
        AlertDialog(
            onDismissRequest = { showValidationDialog = false },
            title = { Text("เกิดข้อผิดพลาด") },
            text = { Text(validationError) },
            confirmButton = {
                Button(onClick = { showValidationDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}
