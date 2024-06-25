package com.example.repaircomputerapplication_finalproject.screens.form

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun formUpdateStatusWork(){
    var repairDetail by remember { mutableStateOf("") }
    var selectedDamageLevel by remember { mutableStateOf("") }
    var Loed_ID by remember { mutableStateOf("") }
    var isExpand by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var departmentName by remember {
        mutableStateOf("")
    }
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD0E6F8)) // Background color
            .padding(16.dp)
            .verticalScroll(scrollState),
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.elevatedCardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = "รหัสคำขอแจ้งซ่อม : test", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "อุปกรณ์:(1)2424", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "ชื่อผู้แจ้ง: 2424 24242", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "หน่วยงาน:242", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "ตึก:242 ห้อง: 333 ชั้นตึก: 1123", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "รายละเอียดอาการเสีย :")
                TextField(
                    value = "322",
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
                    label = { Text("Select Damage Level") },
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
//                    damageLevels?.forEach { level ->
//                        DropdownMenuItem(
//                            text = { Text(text = level.loed_Name.toString()) },
//                            onClick = {
//                                selectedDamageLevel = level.loed_Name.toString()
//                                Loed_ID = level.loed_id.toString()
//                                isExpand = false
//                            }
//                        )
//                    }
                }
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
                    label = { Text("Select Damage Level") },
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
//                    damageLevels?.forEach { level ->
//                        DropdownMenuItem(
//                            text = { Text(text = level.loed_Name.toString()) },
//                            onClick = {
//                                selectedDamageLevel = level.loed_Name.toString()
//                                Loed_ID = level.loed_id.toString()
//                                isExpand = false
//                            }
//                        )
//                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
//                coroutineScope.launch {
//                    viewModel.AddDetail(
//                        requestData?.receive_repair?.rrce_id.toString(),
//                        Loed_ID,
//                        repairDetail
//                    )
//                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color(0xFFCCFFCC), RoundedCornerShape(8.dp))
        ) {
            Text(text = "เพิ่มรายละเอียดการซ่อม", color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    val rrid = "1"
    val navController = rememberNavController()
    formUpdateStatusWork()
}