package com.example.repaircomputerapplication_finalproject.screens.form.formManageData

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
    val typeOptions = listOf("คอมพิวเตอร์", "โทรศัพท์", "แท็บเล็ต")

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
                typeOptions.forEach { option ->
                    DropdownMenuItem(onClick = {
                        selectedType = option
                        showTypeDropdown = false
                    }) {
                        Text(text = option)
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
            onClick = { viewModel.addEquipment(equipmentName, selectedStatus, unit, selectedType) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF8BC34A))
        ) {
            Text(text = "เพิ่มข้อมูล", fontSize = 20.sp, color = Color.White)
        }
    }
}
