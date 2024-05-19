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
fun equipmentTypeForm (isEdit:Boolean?,DataID:String?,viewModel: DataManageViewModel){
    val context = LocalContext.current
    var equipmentType by remember { mutableStateOf("") }
    val eqcList = viewModel.eqc.collectAsState().value
    var btnName by remember { mutableStateOf("เพิ่มข้อมูล") }

    LaunchedEffect(DataID,isEdit){
        viewModel.LoadData()
    }
    LaunchedEffect(eqcList){
        val eqc = eqcList?.find { it.eqc_id.toString() == DataID }
        Log.d(ContentValues.TAG, "buildingForm: $eqcList DataId $DataID eqc:$eqc")
        if(eqc != null && DataID != null && isEdit == true){
            equipmentType = eqc.eqc_name!!
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
            value = equipmentType,
            onValueChange = { equipmentType = it },
            placeholder = { Text("ชื่อประเภทอุปกรณ์") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if(isEdit == true){
                    if(DataID != null){
                        viewModel.editEquipmentType(DataID.toInt(),equipmentType)
                    }else{
                        Toast.makeText(context,"not have DataId", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    viewModel.addEquipmentType(equipmentType)
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