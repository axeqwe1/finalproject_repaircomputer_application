package com.example.repaircomputerapplication_finalproject.screens.form.formManageData

import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.repaircomputerapplication_finalproject.viewModel.ManageViewModel.DataManageViewModel

@Composable
fun buildingForm (isEdit:Boolean?,DataID:String?,viewModel:DataManageViewModel){
    var roomNumber by remember { mutableStateOf("") }
    var floor by remember { mutableStateOf("") }
    var buildingName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB3E5FC))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = roomNumber,
            onValueChange = { roomNumber = it },
            placeholder = { Text("เลขห้อง") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50)
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = floor,
            onValueChange = { floor = it },
            placeholder = { Text("ชั้นตึก") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50)
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = buildingName,
            onValueChange = { buildingName = it },
            placeholder = { Text("ชื่อตึก") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.addBuilding(roomNumber,floor,buildingName) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF8BC34A))
        ) {
            Text(text = "เพิ่มข้อมูล", fontSize = 20.sp, color = Color.Black)
        }
    }

}