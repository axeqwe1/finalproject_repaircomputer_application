package com.example.repaircomputerapplication_finalproject.screens

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeCompilerApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Card
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.repaircomputerapplication_finalproject.model.RequestListResponse
import com.example.repaircomputerapplication_finalproject.viewModel.RequestForRepiarListViewModel
import java.lang.reflect.Array

@Composable
fun RequestRepairScreen(navController: NavHostController, viewModel: RequestForRepiarListViewModel = viewModel()){
    val requestList = viewModel.requestList.collectAsState().value ?: emptyList()
    val eqtList =viewModel.eqtList.collectAsState().value ?: emptyList()
    val empList = viewModel.empList.collectAsState().value ?: emptyList()
    val buildList = viewModel.buildingList.collectAsState().value ?: emptyList()
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        isVisible = true
    }
        if (requestList.isEmpty() || eqtList.isEmpty() || empList.isEmpty() || buildList.isEmpty()) {
            LoadingScreen() // แสดงตัวโหลด
        } else {
            Column(Modifier.fillMaxSize()) {
                // ส่วน UI ทั่วไป
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(requestList) { item ->
                        OutlinedCard(modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                        ){
                            Box(modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Gray)
                                .padding(24.dp)
                            ){
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Text(text = "รหัสแจ้งซ่อม   :${item.rrid}") // ตรงนี้ใช้ข้อมูลจาก item โดยตรง
                                    Text(text = "รายละเอียด    :${item.rr_description}")
                                    Text(text = "ชื่ออุปกรณ์    :${viewModel.getEquipmentName(item.eq_id ?: 0)}")
                                    Text(text = "ชื่อผู้แจ้ง      :${viewModel.getEmployeeFullName(item.employee_id ?: 0)}")
                                    Text(text = "เลขห้อง/ชั้นตึก  :${viewModel.getBuildingInfo(item.building_id ?: 0)}")
                                    // ใช้ข้อมูลอื่นๆ จาก item ได้ที่นี่
                                }
                            }
                        }
                    }
                }
            }
        }

}


