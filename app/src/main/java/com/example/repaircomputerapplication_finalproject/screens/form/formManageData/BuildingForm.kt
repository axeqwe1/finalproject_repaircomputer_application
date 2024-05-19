package com.example.repaircomputerapplication_finalproject.screens.form.formManageData

import android.content.ContentValues.TAG
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.repaircomputerapplication_finalproject.viewModel.ManageViewModel.DataManageViewModel

@Composable
fun buildingForm (isEdit:Boolean?,DataID:String?,viewModel:DataManageViewModel){
    val context = LocalContext.current
    var roomNumber by remember { mutableStateOf("") }
    var floor by remember { mutableStateOf("") }
    var buildingName by remember { mutableStateOf("") }
    var btnName by remember { mutableStateOf("เพิ่มข้อมูล") }
    val buildList = viewModel.building.collectAsState().value
    LaunchedEffect(DataID,isEdit ){
        viewModel.LoadData()
    }
    LaunchedEffect(buildList){
        val build = buildList?.find { it.building_id.toString() == DataID }
        Log.d(TAG, "buildingForm: $buildList DataId $DataID")
        if(build != null && DataID != null && isEdit == true){
            roomNumber = build.building_room_number
            floor = build.building_floor.toString()
            buildingName = build.building_name
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
        if(isEdit == true){
            Text(text = "isEdit == true")
        }else{
            Text(text = "isEdit == false")
        }
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
            onClick = {
                if(isEdit == true){
                    if(DataID != null){
                        viewModel.editBuilding(DataID.toInt(),roomNumber,floor,buildingName)
                    }else{
                        Toast.makeText(context,"not have DataId",Toast.LENGTH_SHORT).show()
                    }
                }else{
                    viewModel.addBuilding(roomNumber,floor,buildingName)
                }

            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF8BC34A))
        ) {
            Text(text = "$btnName", fontSize = 20.sp, color = Color.Black)
        }
    }
}