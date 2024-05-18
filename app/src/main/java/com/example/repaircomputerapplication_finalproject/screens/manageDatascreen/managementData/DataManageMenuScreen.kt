package com.example.repaircomputerapplication_finalproject.screens.manageDatascreen.managementData

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.material.Text
import com.example.repaircomputerapplication_finalproject.data.ScreenRoutes

@Composable
fun dataManageMenuScreen(navController: NavController){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp)
    ) {
    val dataMenuList = DataMenuList().dataMenuList()
    LazyColumn(modifier = Modifier.fillMaxSize()){
        items(dataMenuList.size){ index ->
            Spacer(modifier = Modifier.padding(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(20.dp, RoundedCornerShape(20))
                        .clip(RoundedCornerShape(20))
                        .background(color = Color.Yellow)
                        .clickable { navController.navigate(dataMenuList[index].Route) }
                        .size(120.dp),
                    contentAlignment = Alignment.Center
                ){
                    Text(text = dataMenuList[index].Btn_Name, color = Color.Black, fontSize = 24.sp)
                }
            Spacer(modifier = Modifier.padding(8.dp))
            }
        }
    }
}

private data class DataMenuList(
    val Btn_Name : String = "",
    val Icon : ImageVector = Icons.AutoMirrored.Filled.List,
    val Route : String = ""
){
    fun dataMenuList():List<DataMenuList>{
        var list = listOf<DataMenuList>()
        list = listOf(
            DataMenuList(
                Btn_Name = "หน้าจัดการข้อมูลหน่วยงาน",
                Icon = Icons.AutoMirrored.Filled.List,
                Route = ScreenRoutes.ManageDataScreen.passDataType("Department")
            ),
            DataMenuList(
                Btn_Name = "หน้าจัดการข้อมูลตึก",
                Icon = Icons.AutoMirrored.Filled.List,
                Route = ScreenRoutes.ManageDataScreen.passDataType("Building")
            ),
            DataMenuList(
                Btn_Name = "หน้าจัดการข้อมูลระดับความเสียหาย",
                Icon = Icons.AutoMirrored.Filled.List,
                Route = ScreenRoutes.ManageDataScreen.passDataType("LevelOfDamage")
            ),
            DataMenuList(
                Btn_Name = "หน้าจัดการข้อมูลอุปกรณ์",
                Icon = Icons.AutoMirrored.Filled.List,
                Route = ScreenRoutes.ManageDataScreen.passDataType("Equipment")
            ),
            DataMenuList(
                Btn_Name = "หน้าจัดการข้อมูลประเภทอุปกรณ์",
                Icon = Icons.AutoMirrored.Filled.List,
                Route = ScreenRoutes.ManageDataScreen.passDataType("EquipmentType")
            ),
        )
        return list
    }
}