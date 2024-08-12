package com.example.repaircomputerapplication_finalproject.screens.form.formManageData

import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.repaircomputerapplication_finalproject.viewModel.ManageViewModel.DataManageViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun equipmentForm(isEdit: Boolean?, DataID: String?, viewModel: DataManageViewModel) {
    var equipmentName by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("") }
    var showStatusDropdown by remember { mutableStateOf(false) }
    var selectedStatus by remember { mutableStateOf("สถานะอุปกรณ์") }
    var showTypeDropdown by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf("ประเภทอุปกรณ์") }

    val statusOptions = listOf("พร้อมใช้งาน", "กำลังใช้งาน", "เสียหาย")
    var eqcId by remember { mutableStateOf(0) }
    val context = LocalContext.current
    val eqList = viewModel.eq.collectAsState().value
    val eqcList = viewModel.eqc.collectAsState().value
    var btnName by remember { mutableStateOf("เพิ่มข้อมูล") }
    var showDialog by remember { mutableStateOf(false) }
    var dialogTitle by remember { mutableStateOf("") }
    var dialogMessage by remember { mutableStateOf("") }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var successDialogTitle by remember { mutableStateOf("") }
    var successDialogMessage by remember { mutableStateOf("") }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var confirmationDialogTitle by remember { mutableStateOf("") }
    var confirmationDialogMessage by remember { mutableStateOf("") }
    var onConfirmAction by remember { mutableStateOf<() -> Unit>({}) }

    LaunchedEffect(DataID, isEdit) {
        viewModel.LoadData()
    }
    LaunchedEffect(eqList) {
        val eq = eqList?.find { it.eq_id.toString() == DataID }
        Log.d(ContentValues.TAG, "equipmentForm: $eqList DataId $DataID")
        if (eq != null && DataID != null && isEdit == true) {
            equipmentName = eq.eq_name!!
            unit = eq.eq_unit!!
            selectedStatus = eq.eq_status!!
            selectedType = viewModel.getEquipmentTypeName(eq.eqc_id ?: 0)
            btnName = "แก้ไขข้อมูล"
            eqcId = eq.eqc_id ?: 0
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
        TextField(
            value = equipmentName,
            onValueChange = { equipmentName = it },
            placeholder = { Text("ชื่ออุปกรณ์") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50)
        )

        Spacer(modifier = Modifier.height(16.dp))

        ExposedDropdownMenuBox(
            expanded = showStatusDropdown,
            onExpandedChange = {
                showStatusDropdown = !showStatusDropdown
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                value = selectedStatus,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    Icon(
                        imageVector = if (showStatusDropdown) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.clickable { showStatusDropdown = !showStatusDropdown }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(50),
                placeholder = { Text(text = "สถานะอุปกรณ์") }
            )
            ExposedDropdownMenu(
                expanded = showStatusDropdown,
                onDismissRequest = { showStatusDropdown = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                statusOptions.forEach { option ->
                    DropdownMenuItem(onClick = {
                        selectedStatus = option
                        showStatusDropdown = false
                    }) {
                        Text(text = option)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ExposedDropdownMenuBox(
            expanded = showTypeDropdown,
            onExpandedChange = {
                showTypeDropdown = !showTypeDropdown
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                value = selectedType,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    Icon(
                        imageVector = if (showTypeDropdown) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.clickable { showTypeDropdown = !showTypeDropdown }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(50),
                placeholder = { Text(text = "ประเภทอุปกรณ์") }
            )
            ExposedDropdownMenu(
                expanded = showTypeDropdown,
                onDismissRequest = { showTypeDropdown = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                eqcList?.forEach { option ->
                    DropdownMenuItem(onClick = {
                        selectedType = option.eqc_name
                        eqcId = option.eqc_id!!
                        showTypeDropdown = false
                    }) {
                        Text(text = option.eqc_name)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = unit,
            onValueChange = { unit = it },
            placeholder = { Text("หน่วยอุปกรณ์") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (equipmentName.isBlank() || unit.isBlank() || selectedStatus == "สถานะอุปกรณ์" || selectedType == "ประเภทอุปกรณ์") {
                    dialogTitle = "เกิดข้อผิดพลาด"
                    dialogMessage = "กรุณากรอกข้อมูลให้ครบถ้วน"
                    showDialog = true
                } else {
                    confirmationDialogTitle = if (isEdit == true) "ยืนยันการแก้ไข" else "ยืนยันการเพิ่มข้อมูล"
                    confirmationDialogMessage = if (isEdit == true) "คุณต้องการแก้ไขข้อมูลหรือไม่?" else "คุณต้องการเพิ่มข้อมูลหรือไม่?"
                    onConfirmAction = {
                        if (isEdit == true) {
                            if (DataID != null) {
                                viewModel.editEquipment(DataID.toInt(), equipmentName, selectedStatus, unit, eqcId.toString())
                                successDialogTitle = "สำเร็จ"
                                successDialogMessage = "แก้ไขข้อมูลเรียบร้อยแล้ว"
                                showSuccessDialog = true
                            } else {
                                Toast.makeText(context, "ไม่พบ DataId", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            viewModel.addEquipment(equipmentName, selectedStatus, unit, eqcId.toString())
                            successDialogTitle = "สำเร็จ"
                            successDialogMessage = "เพิ่มข้อมูลเรียบร้อยแล้ว"
                            showSuccessDialog = true
                        }
                    }
                    showConfirmationDialog = true
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF8BC34A))
        ) {
            Text(text = btnName, fontSize = 20.sp, color = Color.White)
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(dialogTitle) },
                text = { Text(dialogMessage) },
                confirmButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("OK")
                    }
                }
            )
        }

        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = { showSuccessDialog = false },
                title = { Text(successDialogTitle) },
                text = { Text(successDialogMessage) },
                confirmButton = {
                    Button(onClick = { showSuccessDialog = false }) {
                        Text("OK")
                    }
                }
            )
        }

        if (showConfirmationDialog) {
            AlertDialog(
                onDismissRequest = { showConfirmationDialog = false },
                title = { Text(confirmationDialogTitle) },
                text = { Text(confirmationDialogMessage) },
                confirmButton = {
                    Button(onClick = {
                        showConfirmationDialog = false
                        onConfirmAction()
                    }) {
                        Text("ยืนยัน")
                    }
                },
                dismissButton = {
                    Button(onClick = { showConfirmationDialog = false }) {
                        Text("ยกเลิก")
                    }
                }
            )
        }
    }
}