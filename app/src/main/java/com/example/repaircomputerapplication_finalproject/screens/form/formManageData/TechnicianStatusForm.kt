package com.example.repaircomputerapplication_finalproject.screens.form.formManageData

import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.repaircomputerapplication_finalproject.viewModel.ManageViewModel.DataManageViewModel

@Composable
fun technicianStatusForm(isEdit: Boolean?, DataID: String?, viewModel: DataManageViewModel) {
    var techStatus by remember { mutableStateOf("") }
    val statusList = viewModel.techStatus.collectAsState().value
    var btnName by remember { mutableStateOf("เพิ่มข้อมูล") }
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var dialogTitle by remember { mutableStateOf("") }
    var dialogMessage by remember { mutableStateOf("") }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var successDialogTitle by remember { mutableStateOf("") }
    var successDialogMessage by remember { mutableStateOf("") }
    var showFailDialog by remember { mutableStateOf(false) }
    var failDialogTitle by remember { mutableStateOf("") }
    var failDialogMessage by remember { mutableStateOf("") }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var confirmationDialogTitle by remember { mutableStateOf("") }
    var confirmationDialogMessage by remember { mutableStateOf("") }
    var onConfirmAction by remember { mutableStateOf<() -> Unit>({}) }

    LaunchedEffect(DataID, isEdit) {
        viewModel.LoadData()
    }
    LaunchedEffect(statusList) {
        val status = statusList?.find { it.status_id.toString() == DataID }
        Log.d(ContentValues.TAG, "technicianStatusForm: $statusList DataId $DataID")
        if (status != null && DataID != null && isEdit == true) {
            techStatus = status.receive_request_status!!
            btnName = "แก้ไขข้อมูล"
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
            value = techStatus,
            onValueChange = {
                if (it.length <= 2) {
                    techStatus = it
                }
            },
            placeholder = { Text("สถานะของช่าง") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (techStatus.isBlank()) {
                    dialogTitle = "เกิดข้อผิดพลาด"
                    dialogMessage = "กรุณากรอกสถานะการรับงาน"
                    showDialog = true
                } else {
                    confirmationDialogTitle = if (isEdit == true) "ยืนยันการแก้ไข" else "ยืนยันการเพิ่มข้อมูล"
                    confirmationDialogMessage = if (isEdit == true) "คุณต้องการแก้ไขข้อมูลหรือไม่?" else "คุณต้องการเพิ่มข้อมูลหรือไม่?"
                    onConfirmAction = {
                        if (isEdit == true) {
                            if (DataID != null) {
                                viewModel.editTechStatus(DataID.toInt(), techStatus)
                                successDialogTitle = "สำเร็จ"
                                successDialogMessage = "แก้ไขข้อมูลเรียบร้อยแล้ว"
                                showSuccessDialog = true
                            } else {
                                failDialogTitle = "เกิดข้อผิดพลาด"
                                failDialogMessage = "ไม่พบ DataId"
                                showFailDialog = true
                            }
                        } else {
                            viewModel.addTechStatus(techStatus)
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
            Text(text = btnName, fontSize = 20.sp, color = Color.Black)
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

        if (showFailDialog) {
            AlertDialog(
                onDismissRequest = { showFailDialog = false },
                title = { Text(failDialogTitle) },
                text = { Text(failDialogMessage) },
                confirmButton = {
                    Button(onClick = { showFailDialog = false }) {
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
