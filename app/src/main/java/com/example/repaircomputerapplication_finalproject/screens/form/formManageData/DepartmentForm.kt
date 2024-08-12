package com.example.repaircomputerapplication_finalproject.screens.form.formManageData

import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
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
fun departmentForm(isEdit: Boolean?, DataID: String?, viewModel: DataManageViewModel) {
    val context = LocalContext.current
    var department by remember { mutableStateOf("") }
    val depList = viewModel.department.collectAsState().value
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
    LaunchedEffect(depList) {
        val dep = depList?.find { it.department_id.toString() == DataID }
        Log.d(ContentValues.TAG, "departmentForm: $depList DataId $DataID")
        if (dep != null && DataID != null && isEdit == true) {
            department = dep.departmentName
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
        Text(text = "ชื่อหน่วยงาน")
        TextField(
            value = department,
            onValueChange = { department = it },
            placeholder = { Text("ชื่อหน่วยงาน") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (department.isBlank()) {
                    dialogTitle = "เกิดข้อผิดพลาด"
                    dialogMessage = "กรุณากรอกชื่อหน่วยงาน"
                    showDialog = true
                } else {
                    confirmationDialogTitle = if (isEdit == true) "ยืนยันการแก้ไข" else "ยืนยันการเพิ่มข้อมูล"
                    confirmationDialogMessage = if (isEdit == true) "คุณต้องการแก้ไขข้อมูลหรือไม่?" else "คุณต้องการเพิ่มข้อมูลหรือไม่?"
                    onConfirmAction = {
                        if (isEdit == true) {
                            if (DataID != null) {
                                viewModel.editDepartment(DataID.toInt(), department)
                                successDialogTitle = "สำเร็จ"
                                successDialogMessage = "แก้ไขข้อมูลเรียบร้อยแล้ว"
                                showSuccessDialog = true
                            } else {
                                Toast.makeText(context, "ไม่พบ DataId", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            viewModel.addDepartment(department)
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
