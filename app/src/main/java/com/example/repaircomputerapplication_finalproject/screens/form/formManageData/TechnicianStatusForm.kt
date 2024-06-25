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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.repaircomputerapplication_finalproject.viewModel.ManageViewModel.DataManageViewModel

@Composable
fun technicianStatusForm (isEdit:Boolean?,DataID:String?,viewModel: DataManageViewModel){
    var techStatus by remember { mutableStateOf("") }
    val statusList = viewModel.techStatus.collectAsState().value
    var btnName by remember { mutableStateOf("เพิ่มข้อมูล") }
    val context = LocalContext.current
    LaunchedEffect(DataID,isEdit){
        viewModel.LoadData()
    }
    LaunchedEffect(statusList){
        val status = statusList?.find { it.status_id.toString() == DataID }
        Log.d(ContentValues.TAG, "buildingForm: $statusList DataId $DataID")
        if(status != null && DataID != null && isEdit == true){
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
            onValueChange = { techStatus = it },
            placeholder = { Text("ชื่อระดับความเสียหาย") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if(isEdit == true){
                    if(DataID != null){
                        viewModel.editTechStatus(DataID.toInt(),techStatus)
                    }else{
                        Toast.makeText(context,"not have DataId", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    viewModel.addTechStatus(techStatus)
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